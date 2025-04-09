package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.ImageView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideMobileActivityIcon : HookRegister() {
    override fun init() = hasEnable("hide_mobile_activity_icon") {
        return@hasEnable
        //隐藏移动箭头
//        findMethod("com.oplusos.systemui.statusbar.OplusStatusBarMobileView") {
//            name == "initViewState"
//        }.hookAfter {
//            (it.thisObject.getObject("mDataActivity") as ImageView).visibility = View.GONE
//        }
//        findMethod("com.oplusos.systemui.statusbar.OplusStatusBarMobileView") {
//            name == "updateMobileViewState"
//        }.hookAfter {
//            (it.thisObject.getObject("mDataActivity") as ImageView).visibility = View.GONE
//        }
    }
}