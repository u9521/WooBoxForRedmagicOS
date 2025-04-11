package com.u9521.wooboxforredmagicos.hook.app.settings

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object FroceDisplayZvioceSwitch : HookRegister() {
    override fun init() = hasEnable("setting_Froce_Display_ZvioceSwitch") {
        MethodFinder.fromClass("com.zte.settings.utils.ZteUtilityUtils")
            .filterByName("needMaskVoiceAssistant")
            .filterByReturnType(Boolean::class.javaPrimitiveType!!).first().createBeforeHook{
                it.result = false
            }
    }
}