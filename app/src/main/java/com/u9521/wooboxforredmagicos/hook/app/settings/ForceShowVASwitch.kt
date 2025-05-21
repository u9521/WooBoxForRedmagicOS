package com.u9521.wooboxforredmagicos.hook.app.settings

import android.content.Context
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object ForceShowVASwitch : HookRegister() {
    override fun init() = hasEnable("setting_force_show_va_switch") {
        val maskVA = getDefaultCL().loadClass("com.zte.settings.utils.ZteUtilityUtils")
            .getDeclaredMethod("needMaskVoiceAssistant", Context::class.java)
        val maskVAHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
                Log.i("MaskVoiceAssistant set to false")
            }
        }
        XposedBridge.hookMethod(maskVA, maskVAHooker)
    }
}