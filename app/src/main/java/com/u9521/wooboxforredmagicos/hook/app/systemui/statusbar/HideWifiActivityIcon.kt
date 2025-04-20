package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.ImageView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideWifiActivityIcon : HookRegister() {
    override fun init() = hasEnable("hide_wifi_activity_icon") {
        //隐藏WIFI箭头
        val sBWView = ClassUtils.loadClass("com.android.systemui.statusbar.StatusBarWifiView")
        MethodFinder.fromClass(sBWView)
            .filterByName("updateState").first().createAfterHook {
                val mInView =
                    it.thisObject.javaClass.getDeclaredField("mIn").get(it.thisObject) as ImageView
                mInView.visibility = View.GONE
            }
        MethodFinder.fromClass(sBWView)
            .filterByName("initViewState").first().createAfterHook {
                val mInView =
                    it.thisObject.javaClass.getDeclaredField("mIn").get(it.thisObject) as ImageView
                mInView.visibility = View.GONE
            }
    }
}