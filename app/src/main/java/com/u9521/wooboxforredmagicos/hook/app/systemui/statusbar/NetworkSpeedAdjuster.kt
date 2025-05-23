package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.net.TrafficStats
import android.os.SystemClock
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.lang.reflect.Method
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.min

object NetworkSpeedAdjuster : HookRegister() {

    private var mLastTotalUp: Long = 0
    private var mLastTotalDown: Long = 0

    private var mLastUPTimeStamp: Long = 0
    private var mLastDownTimeStamp: Long = 0
    private var mLastUpSpeed: Double = 0.0
    private var mLastDownSpeed: Double = 0.0
    private val viewDualSize = XSPUtils.getInt("status_bar_network_speed_dual_row_size", 6)
    private val viewDualWidth = XSPUtils.getInt("status_bar_network_speed_dual_row_width", 35)
    private val lowSpeedHideLevel = XSPUtils.getInt("low_speed_hide_kilo_bytes", -1)
    private val digitLen = XSPUtils.getInt("status_bar_network_speed_dual_row_digit_len", 3)
    private val hideUnitPerSec = XSPUtils.getBoolean("speed_unit_hide_per_second", false)
    private val speedRefreshDelayMs = 1000L

    override fun init() {
        hasEnable("status_bar_dual_row_network_speed") {
            val netSpeedInflate =
                getDefaultCL().loadClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                    .getDeclaredMethod("init")

            val dualLineNetSpeedHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val mSpeedText = param!!.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(param.thisObject) as TextView
                    val mSpeedUnit = param.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(param.thisObject) as TextView
                    val mSpeedViewGroup =
                        param.thisObject.javaClass.getDeclaredField("mSpeedViewGroup")
                            .get(param.thisObject) as ViewGroup
                    val widthPx = if (viewDualWidth > 38) LayoutParams.WRAP_CONTENT else dp2px(
                        mSpeedViewGroup.context, viewDualWidth.toFloat()
                    )
                    mSpeedViewGroup.layoutParams.apply {
                        width = widthPx
                        mSpeedViewGroup.layoutParams = this
                    }
                    mSpeedText.layoutParams.apply {
                        width = widthPx
                        mSpeedText.layoutParams = this
                    }
                    mSpeedUnit.layoutParams.apply {
                        width = widthPx
                        mSpeedUnit.layoutParams = this
                    }
                    mSpeedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, viewDualSize.toFloat())
                    mSpeedUnit.setTextSize(TypedValue.COMPLEX_UNIT_DIP, viewDualSize.toFloat())
                }
            }
            XposedBridge.hookMethod(netSpeedInflate, dualLineNetSpeedHooker)
            // update method
            val speedState =
                getDefaultCL().loadClass("com.zte.feature.speed.StatusBarNetSpeedPolicy\$NetSpeedState")
            val stateSpeedText = speedState.getDeclaredField("speedText")
            val stateSpeedUnit = speedState.getDeclaredField("speedUnit")
            val stateVisible = speedState.getDeclaredField("visible")
            // hide on low speed and set text

            val netSpeedUpdate =
                getDefaultCL().loadClass("com.zte.feature.speed.StatusBarNetSpeedPolicy")
                    .getDeclaredMethod("updateNetSpeedDisplay", speedState)

            val netSpeedUpdateHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val curTimestamp = SystemClock.elapsedRealtime()
                    //太快不更
                    if (curTimestamp - mLastDownTimeStamp < speedRefreshDelayMs || curTimestamp - mLastUPTimeStamp < speedRefreshDelayMs) {
                        param!!.result = null
                        return
                    }
                    val curNetState = speedState.cast(param!!.args[0])
                    updateUpSpeed()
                    updateDownSpeed()
                    stateSpeedText.set(curNetState, formatSpeed(mLastUpSpeed))
                    stateSpeedUnit.set(curNetState, formatSpeed(mLastDownSpeed))
                    if (mLastUpSpeed / 1024 <= lowSpeedHideLevel && mLastDownSpeed / 1024 <= lowSpeedHideLevel) {
                        stateVisible.setBoolean(curNetState, false)
                    } else {
                        stateVisible.setBoolean(curNetState, true)
                    }
                }
            }
            XposedBridge.hookMethod(netSpeedUpdate, netSpeedUpdateHooker)

            //bypass original method,speedup a liiiiiiiiittle
            val updateSpeedVal =
                getDefaultCL().loadClass("com.zte.feature.speed.SpeedControllerImpl")
                    .getDeclaredMethod("updateNetSpeed")
            val speedValHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.thisObject.javaClass.getDeclaredField("mLevel")
                        .setLong(param.thisObject, 114514L)
                    param.result = null
                }
            }
            XposedBridge.hookMethod(updateSpeedVal, speedValHooker)

        }
        hasEnable("status_bar_network_speed_refresh_speed") {
            val delayMethod = getDefaultCL().loadClass("android.os.Handler").getDeclaredMethod(
                "postDelayed", Runnable::class.java, Long::class.javaPrimitiveType
            )

            val runMethod = findNetRunnerClazz(dexKitBridge!!)
            val delayHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (param == null) {
                        return
                    }
                    val runnable = param.args[0] as Runnable
                    if (!runnable.javaClass.name.equals(runMethod.declaringClass.name)) {
                        return
                    }
                    param.args[1] = speedRefreshDelayMs
                }
            }
            val startTimerHooker = object : XC_MethodHook() {
                var hooker: Unhook? = null
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    hooker = XposedBridge.hookMethod(delayMethod, delayHooker)
                }

                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
//                    Log.i("hook net speed update time finished, release postDelayed hooker")
                    hooker!!.unhook()
                }
            }
            XposedBridge.hookMethod(runMethod, startTimerHooker)
        }
    }

    //获取总的上行速度
    private fun updateUpSpeed() {
        val nowTotalTxBytes = TrafficStats.getTotalTxBytes()
        val nowTimeStamp = SystemClock.elapsedRealtime()
        //太快了，忽略这次更新
        if (nowTimeStamp - mLastDownTimeStamp < speedRefreshDelayMs) {
            return
        }
        //计数器重置
        if (nowTotalTxBytes - mLastTotalUp <= 0) {
            this.mLastUpSpeed = 0.0
            this.mLastUPTimeStamp = nowTimeStamp
            this.mLastTotalUp = nowTotalTxBytes
            return
        }
        val upBytesPerSecond =
            (nowTotalTxBytes - mLastTotalUp) * 1000 / (nowTimeStamp - mLastUPTimeStamp).toDouble()
        this.mLastTotalUp = nowTotalTxBytes
        this.mLastUpSpeed = upBytesPerSecond
        this.mLastUPTimeStamp = nowTimeStamp
    }


    //获取总的下行速度
    private fun updateDownSpeed() {
        val currentTotalRxBytes = TrafficStats.getTotalRxBytes()
        val nowTimeStamp = SystemClock.elapsedRealtime()
        if (nowTimeStamp - mLastDownTimeStamp < speedRefreshDelayMs) {
            return
        }
        //计数器重置
        if (currentTotalRxBytes - mLastTotalDown <= 0) {
            this.mLastDownSpeed = 0.0
            this.mLastDownTimeStamp = nowTimeStamp
            this.mLastTotalDown = currentTotalRxBytes
            return
        }
        val downBytesPerSecond =
            (currentTotalRxBytes - mLastTotalDown) * 1000 / (nowTimeStamp - mLastDownTimeStamp).toDouble()
        this.mLastTotalDown = currentTotalRxBytes
        this.mLastDownSpeed = downBytesPerSecond
        this.mLastDownTimeStamp = nowTimeStamp
    }


    private fun formatSpeed(speed: Double): String {
        val units = listOf("B", "KiB", "MiB", "GiB", "TiB")
        var speedCp = speed
        val perSec = if (hideUnitPerSec) "" else "/s"
        var unitIndex = 0
        while (speedCp >= 1024 && unitIndex < units.size - 1) {
            speedCp /= 1024
            unitIndex++
        }
        val df = DecimalFormat()
        val integerDigits = if (speedCp == 0.0) 1 else log10(speedCp).toInt() + 1
        val decimalPlaces = min(4 - integerDigits, digitLen)
        df.maximumFractionDigits = decimalPlaces.coerceAtLeast(0)
        df.isGroupingUsed = false
        return if (unitIndex == units.size - 1 && speedCp > 1000) {
            //seriously ?
            "%.0f".format(speedCp) + units[unitIndex] + perSec
        } else {
            df.format(speedCp) + units[unitIndex] + perSec
        }
    }

    private fun findNetRunnerClazz(bridge: DexKitBridge): Method {
        val methodData = bridge.findClass {
            searchPackages("com.zte.feature.speed")
            matcher {
                interfaces {
                    add("java.lang.Runnable")
                }
                methods {
                    add {
                        name = "run"
                        usingNumbers(0xbb8L)
                    }
                }
            }
        }.findMethod {
            matcher {
                name = "run"
                usingNumbers(0xbb8L)
            }
        }.singleOrNull() ?: error("NetRunnerMethod not find")
        return methodData.getMethodInstance(getDefaultCL())
    }
}