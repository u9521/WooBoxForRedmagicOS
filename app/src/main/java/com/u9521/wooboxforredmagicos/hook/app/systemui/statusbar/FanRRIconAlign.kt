package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object FanRRIconAlign : HookRegister() {
    override fun init() = hasEnable("align_fan_refreshrate_aosp") {
        val fanClazz = ClassUtils.loadClass("com.zte.feature.statusbar.CoolingFansFeature")
        val refreshRateClazz = ClassUtils.loadClass("com.zte.feature.statusbar.RefreshRateFeature")
        MethodFinder.fromClass(fanClazz).filterByName("onStatusBarInflate").first()
            .createAfterHook {
                val cfView = fanClazz.getDeclaredField("coolingFansView").get(it.thisObject) as View
                (cfView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    topMargin = 0
                }
            }
        MethodFinder.fromClass(refreshRateClazz).filterByName("onStatusBarInflate").first()
            .createAfterHook {
                val rrView = refreshRateClazz.getDeclaredField("rateRefreshView").get(it.thisObject) as View
                (rrView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    topMargin = 0
                }
            }

    }
}