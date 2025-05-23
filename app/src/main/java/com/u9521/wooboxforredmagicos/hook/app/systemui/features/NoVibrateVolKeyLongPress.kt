package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object NoVibrateVolKeyLongPress : HookRegister() {
    override fun init() = hasEnable("no_vibrate_volKey_Long_press") {
        val vibrateForVolMe =
            getDefaultCL().loadClass("com.zte.adapt.mifavor.volume.VolumeDialogImplAdapt")
                .getDeclaredMethod("richTapVibrateForVolumeKeyLongPress")
        val vibrateForVolMeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = null
            }
        }
        XposedBridge.hookMethod(vibrateForVolMe, vibrateForVolMeHooker)
    }
}