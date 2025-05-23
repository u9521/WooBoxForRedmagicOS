package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object HideVpnIcon : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("hide_vpn_icon") {
        val vpnEnableMe =
            getDefaultCL().loadClass("com.android.systemui.statusbar.policy.SecurityControllerImpl")
                .getDeclaredMethod("isVpnEnabled")
        val falseHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = false
            }
        }
        XposedBridge.hookMethod(vpnEnableMe, falseHooker)
    }
}