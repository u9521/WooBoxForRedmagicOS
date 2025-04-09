package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveFooterSecurityWarn : HookRegister() {
    override fun init() = hasEnable("remove_footer_security_warn") {
        return@hasEnable
//        findMethod("com.oplusos.systemui.qs.widget.OplusQSSecurityText") {
//            name == "handleRefreshState"
//        }.hookReturnConstant(null)
    }
}