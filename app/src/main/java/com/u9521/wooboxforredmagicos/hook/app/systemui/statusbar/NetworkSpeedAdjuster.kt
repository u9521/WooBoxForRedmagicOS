package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.net.TrafficStats
import android.util.TypedValue
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import java.text.DecimalFormat
import kotlin.math.log10

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

    override fun init() {
        hasEnable("status_bar_dual_row_network_speed") {
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                .filterByName("init").first().createAfterHook {
                    val mSpeedText = it.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(it.thisObject) as TextView
                    val mSpeedUnit = it.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(it.thisObject) as TextView
                    val mSpeedViewGroup =
                        it.thisObject.javaClass.getDeclaredField("mSpeedViewGroup")
                            .get(it.thisObject) as ViewGroup
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
            // update method
            val speedState =
                ClassUtils.loadClass("com.zte.feature.speed.StatusBarNetSpeedPolicy\$NetSpeedState")
            val stateSpeedText = speedState.getDeclaredField("speedText")
            val stateSpeedUnit = speedState.getDeclaredField("speedUnit")
            val stateVisible = speedState.getDeclaredField("visible")
            //sync it
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedPolicy")
                .filterByName("onSpeedLevelChanged").first().createAfterHook {
                    //(String speedText, String speedUnit)
                    val curNetState = speedState.cast(
                        it.thisObject.javaClass.getDeclaredField("mNetSpeedState")
                            .get(it.thisObject)
                    )
                    stateSpeedText.set(curNetState, formatSpeed(mLastUpSpeed))
                    stateSpeedUnit.set(curNetState, formatSpeed(mLastDownSpeed))
                    if (mLastUpSpeed / 1024 <= lowSpeedHideLevel && mLastDownSpeed / 1024 <= lowSpeedHideLevel) {
                        stateVisible.setBoolean(curNetState, false)
                    } else {
                        stateVisible.setBoolean(curNetState, true)
                    }
                }
            // hide on low speed and set text
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedPolicy")
                .filterByName("updateNetSpeedDisplay").first().createBeforeHook {
                    val curNetState = speedState.cast(it.args[0])
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
            //bypass original method,speedup a liiiiiiiiittle
            MethodFinder.fromClass("com.zte.feature.speed.SpeedControllerImpl")
                .filterByName("updateNetSpeed").first().createBeforeHook {
                    it.thisObject.javaClass.getDeclaredField("mLevel")
                        .setLong(it.thisObject, 114514L)
                    it.result = null
                }
        }
        hasEnable("status_bar_network_speed_refresh_speed") {
            val delayMs = 1000L
            val delayMethod =
                MethodFinder.fromClass("android.os.Handler").filterByName("postDelayed")
                    .filterByParamTypes(Runnable::class.java, Long::class.javaPrimitiveType).first()
            lateinit var netRunnerClazzName: String
            DexKitBridge.create(getLoadPackageParam().appInfo.sourceDir)
                .use { dexKitBridge: DexKitBridge ->
                    netRunnerClazzName = findNetRunnerClazz(dexKitBridge)
                }
            val netRunnerClazz = ClassUtils.loadClass(netRunnerClazzName)
            val runMethod = MethodFinder.fromClass(netRunnerClazz).filterByName("run").first()
            val delayHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    if (param == null) {
                        return
                    }
                    val runnable = param.args[0] as Runnable
                    if (!runnable.javaClass.name.equals(netRunnerClazz.name)) {
                        return
                    }
                    param.args[1] = delayMs
                }
            }
            runMethod.createHook {
                var hooker: XC_MethodHook.Unhook? = null
                before {
                    hooker = XposedBridge.hookMethod(delayMethod, delayHooker)
                }
                after {
                    hooker!!.unhook()
                }
            }
        }
    }

    //获取总的上行速度
    private fun updateUpSpeed() {
        val nowTotalTxBytes = TrafficStats.getTotalTxBytes()
        val nowTimeStamp = System.currentTimeMillis()
        //时间回溯了吗
        if (nowTimeStamp - mLastUPTimeStamp <= 0) {
            this.mLastUpSpeed = 0.0
            this.mLastUPTimeStamp = nowTimeStamp
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
        val nowTimeStampTotalDown = System.currentTimeMillis()
        //时间回溯了吗
        if (nowTimeStampTotalDown - mLastDownTimeStamp <= 0) {
            this.mLastDownSpeed = 0.0
            this.mLastDownTimeStamp = nowTimeStampTotalDown
            return
        }
        //计数器重置
        if (currentTotalRxBytes - mLastTotalDown <= 0) {
            this.mLastDownSpeed = 0.0
            this.mLastDownTimeStamp = nowTimeStampTotalDown
            this.mLastTotalDown = currentTotalRxBytes
            return
        }
        val downBytesPerSecond =
            (currentTotalRxBytes - mLastTotalDown) * 1000 / (nowTimeStampTotalDown - mLastDownTimeStamp).toDouble()
        this.mLastTotalDown = currentTotalRxBytes
        this.mLastDownSpeed = downBytesPerSecond
        this.mLastDownTimeStamp = nowTimeStampTotalDown
    }


    private fun formatSpeed(speed: Double): String {
        val units = listOf("B/s", "KiB/s", "MiB/s", "GiB/s", "TiB/s")
        var speedCp = speed
        var unitIndex = 0
        while (speedCp >= 1024 && unitIndex < units.size - 1) {
            speedCp /= 1024
            unitIndex++
        }
        val df = DecimalFormat()
        val integerDigits = if (speedCp == 0.0) 1 else log10(speedCp).toInt() + 1
        val decimalPlaces = 4 - integerDigits
        df.maximumFractionDigits = decimalPlaces.coerceAtLeast(0)
        df.isGroupingUsed = false
        return if (unitIndex == units.size - 1 && speedCp > 1000) {
            //seriously ?
            "%.0f".format(speedCp) + units[unitIndex]
        } else {
            df.format(speedCp) + units[unitIndex]
        }
    }

    private fun findNetRunnerClazz(bridge: DexKitBridge): String {
        val classData = bridge.findClass {
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
        }.singleOrNull() ?: error("NetRunnerClazz not find")
        return classData.name
    }
}