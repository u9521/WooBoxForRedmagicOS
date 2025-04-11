package com.u9521.wooboxforredmagicos.hook.app.settings

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object FunSettingoverrideMAXyear : HookRegister() {
    val maxYearInt = XSPUtils.getInt("fun_override_max_year", 2099)
    override fun init() = hasEnable("setting_Fun_override_maxyear_switch") {
        MethodFinder.fromClass("android.app.timedetector.TimeDetectorHelper")
            .filterByName("getManualDateSelectionYearMax").first().createBeforeHook {
                it.result = maxYearInt
            }
    }
}