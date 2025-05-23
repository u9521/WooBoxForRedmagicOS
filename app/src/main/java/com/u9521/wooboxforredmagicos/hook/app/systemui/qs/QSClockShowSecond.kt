package com.u9521.wooboxforredmagicos.hook.app.systemui.qs


import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object QSClockShowSecond : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("collapsed_status_bar_clock_display_seconds") {

        val clockClazz = getDefaultCL().loadClass("com.android.systemui.statusbar.policy.Clock")
        val showSeconds = clockClazz.getField("mShowSeconds").apply { isAccessible = true }
        val updateShowSeconds = clockClazz.getDeclaredMethod("updateShowSeconds")

        val ccHeaderClazz = getDefaultCL().loadClass("com.zte.controlcenter.widget.CCHeaderView")
        val headerInflateMe = ccHeaderClazz.getDeclaredMethod("onFinishInflate")

        val headerInflateMeHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val clockView = ccHeaderClazz.getField("mClockView").get(param!!.thisObject)
                showSeconds.setBoolean(clockView, true)
                updateShowSeconds.invoke(clockView)
            }
        }
        XposedBridge.hookMethod(headerInflateMe, headerInflateMeHooker)

    }
}