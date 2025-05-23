package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import android.content.ComponentName
import android.os.Bundle
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object GestureStartDefaultDigitalAssist : HookRegister() {
    override fun init() = hasEnable("gesture_use_default_digital_assist") {
        //adapt 返回默认数字助理
        val adaptClazz = getDefaultCL().loadClass("com.zte.adapt.mifavor.navbar.AssistManagerAdapt")
        val getAssistMe =
            adaptClazz.getDeclaredMethod("getAssistInfoForUser", ComponentName::class.java)
        val getAssistMeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = param.args[0]
            }
        }
        XposedBridge.hookMethod(getAssistMe, getAssistMeHooker)
        //不要接管
        val handleSAMe = adaptClazz.getDeclaredMethod("handleStartAssist", Bundle::class.java)
        val handleSAMeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(handleSAMe, handleSAMeHooker)
    }
}
