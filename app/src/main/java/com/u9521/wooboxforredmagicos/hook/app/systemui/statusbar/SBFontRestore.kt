package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object SBFontRestore : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("statusbar_font_restore") {
            //clock
            val clockInflate =
                getDefaultCL().loadClass("com.android.systemui.statusbar.phone.PhoneStatusBarView")
                    .getDeclaredMethod("onFinishInflate")
            val netSpeedInflate =
                getDefaultCL().loadClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                    .getDeclaredMethod("init")
            val batteryPercentageInflate =
                getDefaultCL().loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
                    .getDeclaredMethod("onFinishInflate")

            val clockHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val mClock = param!!.thisObject.javaClass.getDeclaredField("mClock")
                        .get(param.thisObject) as TextView
                    mClock.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mClock.fontFeatureSettings = ""
                    mClock.layoutParams.apply {
                        width = LayoutParams.WRAP_CONTENT
                        mClock.layoutParams = this
                    }
                }
            }
            //network speed
            val netSpeedHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val mSpeedText = param!!.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(param.thisObject) as TextView
                    val mSpeedUnit = param.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(param.thisObject) as TextView
                    mSpeedText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mSpeedUnit.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            }
            //battery percentage
            val batteryPercentageHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val mBatteryLevelInsideView =
                        param!!.thisObject.javaClass.getDeclaredField("mBatteryLevelInsideView")
                            .get(param.thisObject) as TextView
                    val mBatteryLevelOutsideView =
                        param.thisObject.javaClass.getDeclaredField("mBatteryLevelOutsideView")
                            .get(param.thisObject) as TextView
                    mBatteryLevelInsideView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mBatteryLevelOutsideView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            }
            XposedBridge.hookMethod(clockInflate, clockHooker)
            XposedBridge.hookMethod(netSpeedInflate, netSpeedHooker)
            XposedBridge.hookMethod(batteryPercentageInflate, batteryPercentageHooker)
        }
    }
}