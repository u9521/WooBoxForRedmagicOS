package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object BatteryIconAdjuster : HookRegister() {
    override fun init() {
        //充满也显示充电图标
        hasEnable("show_charge_indicator_on_power_plugin") {
            //int batteryLevel, boolean isplugIn, boolean isCharging, boolean isWirelessCharging,
            // int oemFastChargeType 1-> fast 7->superfast
            val bLevelChange =
                getDefaultCL().loadClass("com.zte.mifavor.views.ChargeIndicator").getDeclaredMethod(
                    "onBatteryLevelChanged",
                    Int::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
            val bLevelChangeHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    // let isplugin instead of ischarging
                    param!!.args[2] = param.args[1]
                }
            }
            XposedBridge.hookMethod(bLevelChange, bLevelChangeHooker)
        }
        //隐藏电量百分号
        hasEnable("hide_battery_percentage_icon") {
            val bVLayout = getDefaultCL().loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
            val updateLevel = bVLayout.getDeclaredMethod("updateBatteryLevelText")
            val updateLevelHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val mCurrentUsedBatteryLevelView =
                        bVLayout.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .get(param!!.thisObject) as TextView
                    //typo? LOL
                    val mBatteryLevel =
                        bVLayout.getDeclaredField("mBateryLevel").getInt(param.thisObject)

                    mCurrentUsedBatteryLevelView.text = mBatteryLevel.toString()
                    param.result = null
                }
            }
            XposedBridge.hookMethod(updateLevel, updateLevelHooker)
        }
        //电池图标调节
        hasEnable("battery_icon_fine_tune") {
            val bVLayout = getDefaultCL().loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
            val chargeIndicator = getDefaultCL().loadClass("com.zte.mifavor.views.ChargeIndicator")
            val showMeter = XSPUtils.getBoolean("show_battery_meter", false)
            val showIndicatorInside = XSPUtils.getBoolean("show_charge_indicator_inside", false)
            val showIndicatorOutside = XSPUtils.getBoolean("show_charge_indicator_outside", false)
            //先设置可见性
            val upBatteryL = bVLayout.getDeclaredMethod(
                "updateBatteryLayout",
                Int::class.javaPrimitiveType,
                Boolean::class.javaPrimitiveType
            )
            val batteryVisibilityHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val mBatteryContainer = bVLayout.getDeclaredField("mBatteryContainer")
                        .get(param!!.thisObject) as FrameLayout
                    val mInsideChargeView = bVLayout.getDeclaredField("mInsideChargeView")
                        .get(param.thisObject) as ImageView
                    val mOutsideChargeView = bVLayout.getDeclaredField("mOutsideChargeView")
                        .get(param.thisObject) as ImageView

                    val mCurrentChargeView = bVLayout.getDeclaredField("mCurrentChargeView")

                    mBatteryContainer.visibility = if (showMeter) View.VISIBLE else View.GONE

                    mInsideChargeView.visibility =
                        if (showIndicatorInside) View.VISIBLE else View.GONE

                    mOutsideChargeView.visibility =
                        if (showIndicatorOutside && !showIndicatorInside) View.VISIBLE else View.GONE

                    //充电图标可见性
                    chargeIndicator.getDeclaredMethod(
                        "updateVisibility", Boolean::class.javaPrimitiveType
                    ).invoke(mInsideChargeView, showIndicatorInside)
                    chargeIndicator.getDeclaredMethod(
                        "updateVisibility", Boolean::class.javaPrimitiveType
                    ).invoke(mOutsideChargeView, showIndicatorOutside && !showIndicatorInside)

                    if (showIndicatorInside) {
                        mCurrentChargeView.set(param.thisObject, mInsideChargeView)
                    } else if (showIndicatorOutside) {
                        mCurrentChargeView.set(param.thisObject, mOutsideChargeView)
                    }
                }
            }
            XposedBridge.hookMethod(upBatteryL, batteryVisibilityHooker)

            //充电图标
            val updateChargeViewColor = bVLayout.getDeclaredMethod("updateChargeViewColor")

            data class ChargeIndicatorColorHooker(val isInside: Boolean) : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val currView = if (isInside)
                        bVLayout.getDeclaredField("mInsideChargeView")
                            .get(param!!.thisObject) as View
                    else bVLayout.getDeclaredField("mOutsideChargeView")
                        .get(param!!.thisObject) as View
                    bVLayout.getDeclaredField("mCurrentChargeView")
                        .set(param.thisObject, currView)
                }
            }
            if (showIndicatorInside) {
                XposedBridge.hookMethod(updateChargeViewColor, ChargeIndicatorColorHooker(true))
            } else if (showIndicatorOutside) {
                XposedBridge.hookMethod(updateChargeViewColor, ChargeIndicatorColorHooker(false))
            }
        }
    }
}