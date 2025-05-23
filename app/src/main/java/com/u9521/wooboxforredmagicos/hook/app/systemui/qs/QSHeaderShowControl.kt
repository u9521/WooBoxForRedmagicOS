package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object QSHeaderShowControl : HookRegister() {
    override fun init() {
        val showCarrier = XSPUtils.getBoolean("qs_show_carrier", false)
        val showSearch = XSPUtils.getBoolean("qs_show_search", false)

        val ccHeaderClazz = getDefaultCL().loadClass("com.zte.controlcenter.widget.CCHeaderView")

        val showCarrierHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = showCarrier
            }
        }

        val showSearchHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = showSearch
            }
        }
        val showCarrierMe = ccHeaderClazz.getDeclaredMethod("shouldQsCarrierVisible")

        val showSearchMe = ccHeaderClazz.getDeclaredMethod("showSearchButton")

        XposedBridge.hookMethod(showCarrierMe, showCarrierHooker)

        XposedBridge.hookMethod(showSearchMe, showSearchHooker)
    }
}