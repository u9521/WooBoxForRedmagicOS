package com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object AllowAdjustVolume : HookRegister() {
    override fun init() = hasEnable("lockscreen_allow_adjust_volume") {
        val kgHandleVolKey =
            getDefaultCL().loadClass("com.zte.feature.volume.MfvVolumeDialog")
                .getDeclaredMethod("shouldKeyguardHandleVolumeKeys")
        val kgVolKeyHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(kgHandleVolKey,kgVolKeyHooker)
    }
}