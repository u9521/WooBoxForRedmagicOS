package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object GestureStartDefaultDigitalAssist : HookRegister() {
    override fun init() = hasEnable("gesture_use_default_digital_assist") {
        //adapt 返回默认数字助理
        MethodFinder.fromClass("com.zte.adapt.mifavor.navbar.AssistManagerAdapt")
            .filterByName("getAssistInfoForUser").first().createBeforeHook {
                it.result = it.args[0]
            }
        //不要接管
        MethodFinder.fromClass("com.zte.adapt.mifavor.navbar.AssistManagerAdapt")
            .filterByName("handleStartAssist").first().createBeforeHook {
                it.result = false
            }

    }
}
