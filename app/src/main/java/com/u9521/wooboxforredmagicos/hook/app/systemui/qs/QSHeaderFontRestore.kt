package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.graphics.Typeface
import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object QSHeaderFontRestore : HookRegister() {
    override fun init() {
        hasEnable("statusbar_font_restore") {
            val ccHeaderClazz =
                getDefaultCL().loadClass("com.zte.controlcenter.widget.CCHeaderView")
            val updateResMe = ccHeaderClazz.getDeclaredMethod("updateHeaderResources")
            val textFontHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val dateView =
                        param!!.thisObject.javaClass.getDeclaredField("mDateView")
                            .get(param.thisObject) as TextView

                    val clockView =
                        ccHeaderClazz.getDeclaredField("mClockView")
                            .get(param.thisObject) as TextView

                    val carrierTextView =
                        ccHeaderClazz.getDeclaredField("mCarrierText")
                            .get(param.thisObject) as TextView

                    dateView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)

                    clockView.setTypeface(Typeface.DEFAULT)

                    clockView.fontFeatureSettings = ""

                    carrierTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            }
            XposedBridge.hookMethod(updateResMe, textFontHooker)

        }
    }
}