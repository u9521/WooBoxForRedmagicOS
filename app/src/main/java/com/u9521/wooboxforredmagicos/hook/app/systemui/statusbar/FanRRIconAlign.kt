package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object FanRRIconAlign : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("align_fan_refreshrate_aosp") {
        val fanClazz = getDefaultCL().loadClass("com.zte.feature.statusbar.CoolingFansFeature")
        val refreshRateClazz =
            getDefaultCL().loadClass("com.zte.feature.statusbar.RefreshRateFeature")
        val sBVClazz =
            getDefaultCL().loadClass("com.android.systemui.statusbar.phone.PhoneStatusBarView")
        val fanMe = fanClazz.getDeclaredMethod(
            "onStatusBarInflate", sBVClazz
        )
        val fanHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val cfView =
                    fanClazz.getDeclaredField("coolingFansView").get(param!!.thisObject) as View
                (cfView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    topMargin = 0
                }
            }
        }
        val rrMe = refreshRateClazz.getDeclaredMethod(
            "onStatusBarInflate", sBVClazz
        )
        val rrHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val rrView = refreshRateClazz.getDeclaredField("rateRefreshView")
                    .get(param!!.thisObject) as View
                (rrView.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 0
                    topMargin = 0
                }
            }
        }
        XposedBridge.hookMethod(fanMe, fanHooker)
        XposedBridge.hookMethod(rrMe, rrHooker)
//        MethodFinder.fromClass(fanClazz).filterByName("onStatusBarInflate").first()
//            .createAfterHook {
//                val cfView = fanClazz.getDeclaredField("coolingFansView").get(it.thisObject) as View
//                (cfView.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                    marginStart = 0
//                    topMargin = 0
//                }
//            }
//        MethodFinder.fromClass(refreshRateClazz).filterByName("onStatusBarInflate").first()
//            .createAfterHook {
//                val rrView =
//                    refreshRateClazz.getDeclaredField("rateRefreshView").get(it.thisObject) as View
//                (rrView.layoutParams as ViewGroup.MarginLayoutParams).apply {
//                    marginStart = 0
//                    topMargin = 0
//                }
//            }
    }
}