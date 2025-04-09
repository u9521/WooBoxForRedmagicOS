package com.u9521.wooboxforredmagicos.hook.app.android

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveVPNActivatedNotification : HookRegister() {
    override fun init() {
        return
//        findMethod("com.android.server.connectivity.OplusVpnHelper") {
//            name == "showNotification" && parameterCount == 6
//        }.hookBefore {
//            hasEnable("remove_vpn_activated") {
//                it.result = null
//            }
//        }
    }
}