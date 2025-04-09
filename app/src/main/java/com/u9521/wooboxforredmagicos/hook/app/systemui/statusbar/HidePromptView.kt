package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HidePromptView : HookRegister() {
    override fun init() = hasEnable("hide_prompt_view") {
        findMethod("com.oplusos.systemui.statusbar.widget.SystemPromptView") {
            name == "updateViewVisible"
        }.hookReturnConstant(null)

        findMethod("com.oplusos.systemui.statusbar.widget.SystemPromptView") {
            name == "setViewVisibleByDisable"
        }.hookBefore {
            it.args[0] = true
        }
    }
}