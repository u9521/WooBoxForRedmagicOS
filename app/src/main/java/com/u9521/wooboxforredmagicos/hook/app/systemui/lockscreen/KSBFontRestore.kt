package com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object KSBFontRestore : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("statusbar_font_restore") {
            val cCHVClazz =
                getDefaultCL().loadClass("com.android.systemui.statusbar.phone.KeyguardStatusBarView")
            val inflateMe = cCHVClazz.getDeclaredMethod("onFinishInflate")
            val inflateMeHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val carrierTextView =
                        param!!.thisObject.javaClass.getDeclaredField("mCarrierLabel")
                            .get(param.thisObject) as TextView
                    carrierTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            }
            XposedBridge.hookMethod(inflateMe, inflateMeHooker)
        }
    }
}