package com.u9521.wooboxforredmagicos.hook.app.settings

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object FroceDisplayWakeupGoogleAssistantSwitch : HookRegister() {
    override fun init() = hasEnable("setting_Froce_Display_WakeupGoogleAssistSwitch") {
        MethodFinder.fromClass("com.zte.settings.utils.ZteUtilityUtils")
            .filterByName("needMaskWakeupGoogleAssistant")
            .filterByReturnType(Boolean::class.javaPrimitiveType!!).first().createBeforeHook{
                it.result = false
            }
    }
}