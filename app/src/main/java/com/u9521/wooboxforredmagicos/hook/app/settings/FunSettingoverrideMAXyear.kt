package com.u9521.wooboxforredmagicos.hook.app.settings

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object FunSettingoverrideMAXyear : HookRegister() {
    private val maxYearInt = XSPUtils.getInt("fun_override_max_year", 2099)

    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("setting_fun_override_maxyear_sw") {
        val maxSelYear = getDefaultCL().loadClass("android.app.timedetector.TimeDetectorHelper")
            .getDeclaredMethod("getManualDateSelectionYearMax")
        val maxSelYearHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = maxYearInt
                Log.i("hooked max select year to $maxYearInt")
            }
        }
        XposedBridge.hookMethod(maxSelYear, maxSelYearHooker)
    }
}