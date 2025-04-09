package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import com.github.kyuubiran.ezxhelper.utils.loadClass
import com.github.kyuubiran.ezxhelper.utils.putStaticObject
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object UnlockSelfStartQuantity : HookRegister() {
    override fun init() = hasEnable("unlock_self_start_quantity") {
        findMethod("com.oplus.safecenter.startupapp.a") {
            name == "b"
        }.hookAfter {
            loadClass("com.oplus.safecenter.startupapp.a").putStaticObject("d", 999)
        }
    }
}