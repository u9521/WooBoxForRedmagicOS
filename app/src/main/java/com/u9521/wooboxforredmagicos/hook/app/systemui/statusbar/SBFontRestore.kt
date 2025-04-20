package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.graphics.Typeface
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object SBFontRestore : HookRegister() {
    override fun init() {
        hasEnable("statusbar_font_restore") {
            val clockClazz = ClassUtils.loadClass("com.android.systemui.statusbar.policy.Clock")
            MethodFinder.fromClass(clockClazz).filterByName("onAttachedToWindow").first()
                .createAfterHook {
                    (it.thisObject as TextView).setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    (it.thisObject as TextView).fontFeatureSettings = ""
                    val lp = (it.thisObject as TextView).layoutParams
                    lp.width = ViewGroup.LayoutParams.WRAP_CONTENT
//                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    (it.thisObject as TextView).layoutParams = lp
                }
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                .filterByName("init").first().createAfterHook {
                    val SpeedText = it.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(it.thisObject) as TextView
                    val SpeedUnit = it.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(it.thisObject) as TextView
                    val SpeedViewGroup = it.thisObject.javaClass.getDeclaredField("mSpeedViewGroup")
                        .get(it.thisObject) as ViewGroup
                    SpeedText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    SpeedText.width =LayoutParams.WRAP_CONTENT
                    SpeedUnit.width =LayoutParams.WRAP_CONTENT
                    SpeedUnit.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    SpeedViewGroup.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
                }
        }
    }
}