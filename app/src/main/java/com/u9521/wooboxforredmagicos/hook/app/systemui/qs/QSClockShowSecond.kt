package com.u9521.wooboxforredmagicos.hook.app.systemui.qs


import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object QSClockShowSecond : HookRegister() {
    override fun init() = hasEnable("collapsed_status_bar_clock_display_seconds") {
        val clockClazz = ClassUtils.loadClass("com.android.systemui.statusbar.policy.Clock")
        val showSeconds = clockClazz.getField("mShowSeconds")
        val updateShowSeconds = clockClazz.getDeclaredMethod("updateShowSeconds")
        showSeconds.isAccessible = true
        MethodFinder.fromClass("com.zte.controlcenter.widget.CCHeaderView")
            .filterByName("onFinishInflate").first().createAfterHook {
                val clockp = it.thisObject.javaClass.getField("mClockView").get(it.thisObject)
                showSeconds.setBoolean(clockp, true)
                updateShowSeconds.invoke(clockp)
            }
    }
}