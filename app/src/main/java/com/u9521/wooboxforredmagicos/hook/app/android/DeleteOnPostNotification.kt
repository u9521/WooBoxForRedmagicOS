package com.u9521.wooboxforredmagicos.hook.app.android

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object DeleteOnPostNotification : HookRegister() {
    override fun init() {
        return
//        findMethod("com.android.server.wm.AlertWindowNotification") {
//            name == "onPostNotification"
//        }.hookBefore {
//            hasEnable("delete_on_post_notification") {
//                it.result = null
//            }
//        }
    }
}