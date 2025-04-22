package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object BatteryIconAdjuster : HookRegister() {
    override fun init() {
        //充满也显示充电图标
        hasEnable("show_charge_indicator_on_power_plugin") {
            val chargeIndicator = ClassUtils.loadClass("com.zte.mifavor.views.ChargeIndicator")
            MethodFinder.fromClass(chargeIndicator).filterByName("onBatteryLevelChanged").first()
                .createBeforeHook {
                    //int batteryLevel, boolean isplugIn, boolean isCharging, boolean isWirelessCharging, int oemFastChargeType 1-> fast 7->superfast
                    // let isplugin instead of ischarging
                    it.args[2] = it.args[1]
                }
        }
        //隐藏电量百分号
        hasEnable("hide_battery_percentage_icon") {
            val mFVBVLayout = ClassUtils.loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
            MethodFinder.fromClass(mFVBVLayout).filterByName("updateBatteryLevelText").first()
                .createBeforeHook {
                    val mCurrentUsedBatteryLevelView =
                        it.thisObject.javaClass.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .get(it.thisObject) as TextView
                    val mBateryLevel = it.thisObject.javaClass.getDeclaredField("mBateryLevel")
                        .getInt(it.thisObject)
                    mCurrentUsedBatteryLevelView.text = mBateryLevel.toString()
                    it.result = null
                }
        }
        //电池图标调节
        hasEnable("battery_icon_fine_tune") {
            val bVLayout = ClassUtils.loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
            val chargeIndicator = ClassUtils.loadClass("com.zte.mifavor.views.ChargeIndicator")
            val showMeter = XSPUtils.getBoolean("show_battery_meter", false)
            val showLevelInside = XSPUtils.getBoolean("show_battery_level_inside", false)
            val showLevelOutside = XSPUtils.getBoolean("show_battery_level_outside", false)
            val showIndicatorInside = XSPUtils.getBoolean("show_charge_indicator_inside", false)
            val showIndicatorOutside = XSPUtils.getBoolean("show_charge_indicator_outside", false)
            //先设置可见性
            MethodFinder.fromClass(bVLayout).filterByName("updateBatteryLayout")
                .filterByParamTypes(Int::class.javaPrimitiveType, Boolean::class.javaPrimitiveType)
                .first()
                .createAfterHook {
                    val mBatteryContainer = bVLayout.getDeclaredField("mBatteryContainer")
                        .get(it.thisObject) as FrameLayout
                    val mBatteryLevelInsideView =
                        bVLayout.getDeclaredField("mBatteryLevelInsideView")
                            .get(it.thisObject) as TextView
                    val mBatteryLevelOutsideView =
                        bVLayout.getDeclaredField("mBatteryLevelOutsideView")
                            .get(it.thisObject) as TextView
                    val mInsideChargeView = bVLayout.getDeclaredField("mInsideChargeView")
                        .get(it.thisObject) as ImageView
                    val mOutsideChargeView = bVLayout.getDeclaredField("mOutsideChargeView")
                        .get(it.thisObject) as ImageView
                    mBatteryContainer.visibility = if (showMeter) View.VISIBLE else View.GONE
                    mBatteryLevelInsideView.visibility =
                        if (showLevelInside) View.VISIBLE else View.GONE
                    mBatteryLevelOutsideView.visibility =
                        if (showLevelOutside) View.VISIBLE else View.GONE
                    mInsideChargeView.visibility =
                        if (showIndicatorInside) View.VISIBLE else View.GONE
                    mOutsideChargeView.visibility =
                        if (showIndicatorOutside) View.VISIBLE else View.GONE
                    //充电图标可见性
                    chargeIndicator.getDeclaredMethod(
                        "updateVisibility",
                        Boolean::class.javaPrimitiveType
                    ).invoke(mInsideChargeView, showIndicatorInside)
                    chargeIndicator.getDeclaredMethod(
                        "updateVisibility",
                        Boolean::class.javaPrimitiveType
                    ).invoke(mOutsideChargeView, showIndicatorOutside)
                }
            //电量百分比更新
            if (showLevelInside) {
                MethodFinder.fromClass(bVLayout).filterByName("updateBatteryLevel").first()
                    .createBeforeHook {
                        val mBatteryLevelInsideView =
                            bVLayout.getDeclaredField("mBatteryLevelInsideView")
                                .get(it.thisObject) as TextView
                        bVLayout.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .set(it.thisObject, mBatteryLevelInsideView)
                    }
                //颜色更新
                MethodFinder.fromClass(bVLayout).filterByName("updateBatteryLevelColor").first()
                    .createBeforeHook {
                        val mBatteryLevelInsideView =
                            bVLayout.getDeclaredField("mBatteryLevelInsideView")
                                .get(it.thisObject) as TextView
                        bVLayout.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .set(it.thisObject, mBatteryLevelInsideView)
                    }

            }
            if (showLevelOutside) {
                MethodFinder.fromClass(bVLayout).filterByName("updateBatteryLevel").first()
                    .createBeforeHook {
                        val mBatteryLevelOutsideView =
                            bVLayout.getDeclaredField("mBatteryLevelOutsideView")
                                .get(it.thisObject) as TextView
                        bVLayout.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .set(it.thisObject, mBatteryLevelOutsideView)
                    }
                MethodFinder.fromClass(bVLayout).filterByName("updateBatteryLevelColor").first()
                    .createBeforeHook {
                        val mBatteryLevelOutsideView =
                            bVLayout.getDeclaredField("mBatteryLevelOutsideView")
                                .get(it.thisObject) as TextView
                        bVLayout.getDeclaredField("mCurrentUsedBatteryLevelView")
                            .set(it.thisObject, mBatteryLevelOutsideView)
                    }
            }
            //充电图标
            if (showIndicatorInside) {
                MethodFinder.fromClass(bVLayout).filterByName("updateChargeViewColor").first()
                    .createBeforeHook {
                        val mInsideChargeView =
                            bVLayout.getDeclaredField("mInsideChargeView")
                                .get(it.thisObject) as View
                        bVLayout.getDeclaredField("mCurrentChargeView")
                            .set(it.thisObject, mInsideChargeView)
                    }
            }
            if (showIndicatorOutside) {
                MethodFinder.fromClass(bVLayout).filterByName("updateChargeViewColor").first()
                    .createBeforeHook {
                        val mOutsideChargeView =
                            bVLayout.getDeclaredField("mOutsideChargeView")
                                .get(it.thisObject) as View
                        bVLayout.getDeclaredField("mCurrentChargeView")
                            .set(it.thisObject, mOutsideChargeView)
                    }
            }

        }
    }
}