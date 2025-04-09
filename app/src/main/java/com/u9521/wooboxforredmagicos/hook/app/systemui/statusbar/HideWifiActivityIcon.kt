package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.ImageView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideWifiActivityIcon : HookRegister() {
    override fun init() = hasEnable("hide_wifi_activity_icon") {
        return@hasEnable
        //隐藏WIFI箭头
//        findMethod("com.oplusos.systemui.statusbar.OplusStatusBarWifiView") {
//            name == "initViewState"
//        }.hookAfter {
//            (it.thisObject.getObject("mWifiActivity") as ImageView).visibility = View.GONE
//        }
//        findMethod("com.oplusos.systemui.statusbar.OplusStatusBarWifiView") {
//            name == "updateState"
//        }.hookAfter {
//            (it.thisObject.getObject("mWifiActivity") as ImageView).visibility = View.GONE
//        }
    }
}