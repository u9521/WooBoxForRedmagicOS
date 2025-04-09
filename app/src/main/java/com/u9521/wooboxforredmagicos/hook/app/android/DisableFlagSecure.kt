package com.u9521.wooboxforredmagicos.hook.app.android


import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object DisableFlagSecure : HookRegister() {
    override fun init() {
        return
//        findMethod("com.android.server.wm.WindowState") {
//            name == "isSecureLocked"
//        }.hookBefore {
//            hasEnable("disable_flag_secure") {
//                it.result = false
//            }
//        }
    }
}