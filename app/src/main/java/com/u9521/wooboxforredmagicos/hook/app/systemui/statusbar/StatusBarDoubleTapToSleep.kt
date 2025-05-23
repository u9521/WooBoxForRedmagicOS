package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import android.content.Context
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.view.ViewGroup
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import kotlin.math.abs

object StatusBarDoubleTapToSleep : HookRegister() {

    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("status_bar_double_tap_to_sleep") {
        val onFinishInflateMe =
            getDefaultCL().loadClass("com.android.systemui.statusbar.phone.PhoneStatusBarView")
                .getDeclaredMethod("onFinishInflate")
        val dbClickHooker = object : XC_MethodHook() {
            var currentTouchTime = 0L
            var currentTouchX = 0f
            var currentTouchY = 0f
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val view = param!!.thisObject as ViewGroup
                view.setOnTouchListener(OnTouchListener { v, event ->
                    if (event.action != MotionEvent.ACTION_DOWN) return@OnTouchListener false
                    val lastTouchTime = currentTouchTime
                    val lastTouchX = currentTouchX
                    val lastTouchY = currentTouchY
                    currentTouchTime = System.currentTimeMillis()
                    currentTouchX = event.x
                    currentTouchY = event.y
                    if (currentTouchTime - lastTouchTime < 250L && abs(currentTouchX - lastTouchX) < 100f && abs(
                            currentTouchY - lastTouchY
                        ) < 100f
                    ) {
                        XposedHelpers.callMethod(
                            v.context.getSystemService(Context.POWER_SERVICE),
                            "goToSleep",
                            SystemClock.uptimeMillis()
                        )
                        currentTouchTime = 0L
                        currentTouchX = 0f
                        currentTouchY = 0f
                    }
                    v.performClick()
                    false
                })
            }
        }
        XposedBridge.hookMethod(onFinishInflateMe, dbClickHooker)
    }
}
