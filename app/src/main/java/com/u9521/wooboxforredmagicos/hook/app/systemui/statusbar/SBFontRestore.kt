package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.graphics.Typeface
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
                    lp.width = LayoutParams.WRAP_CONTENT
//                    lp.height = ViewGroup.LayoutParams.WRAP_CONTENT
                    (it.thisObject as TextView).layoutParams = lp
                }
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                .filterByName("init").first().createAfterHook {
                    val mSpeedText = it.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(it.thisObject) as TextView
                    val mSpeedUnit = it.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(it.thisObject) as TextView
                    mSpeedText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mSpeedUnit.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            MethodFinder.fromClass("com.zte.mifavor.views.MFVBatteryViewLayout")
                .filterByName("onFinishInflate").first().createAfterHook {
                    val mBatteryLevelInsideView =
                        it.thisObject.javaClass.getDeclaredField("mBatteryLevelInsideView")
                            .get(it.thisObject) as TextView
                    val mBatteryLevelOutsideView =
                        it.thisObject.javaClass.getDeclaredField("mBatteryLevelOutsideView")
                            .get(it.thisObject) as TextView
                    mBatteryLevelInsideView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mBatteryLevelOutsideView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
        }
    }
}