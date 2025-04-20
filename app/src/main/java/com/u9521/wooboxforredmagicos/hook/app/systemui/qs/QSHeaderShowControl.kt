package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object QSHeaderShowControl : HookRegister() {
    override fun init() {
        val showCarrier = XSPUtils.getBoolean("qs_show_carrier", false)
        val showSearch = XSPUtils.getBoolean("qs_show_search", false)

        val cCHVClazz = ClassUtils.loadClass("com.zte.controlcenter.widget.CCHeaderView")
        MethodFinder.fromClass(cCHVClazz).filterByName("shouldQsCarrierVisible").first()
            .createBeforeHook {
            it.result= showCarrier
            }
        MethodFinder.fromClass(cCHVClazz).filterByName("showSearchButton").first()
            .createAfterHook {
                it.result= showSearch
            }
    }
}