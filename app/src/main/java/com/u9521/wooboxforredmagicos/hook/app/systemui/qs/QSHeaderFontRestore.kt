package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.graphics.Typeface
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister


object QSHeaderFontRestore : HookRegister() {
    override fun init() {
        hasEnable("statusbar_font_restore") {
            val cCHVClazz = ClassUtils.loadClass("com.zte.controlcenter.widget.CCHeaderView")
            MethodFinder.fromClass(cCHVClazz).filterByName("updateHeaderResources").first()
                .createAfterHook {
                    val dateView =
                        it.thisObject.javaClass.getDeclaredField("mDateView")
                            .get(it.thisObject) as TextView
                    val clockView =
                        it.thisObject.javaClass.getDeclaredField("mClockView")
                            .get(it.thisObject) as TextView
                    val carrierTextView =
                        it.thisObject.javaClass.getDeclaredField("mCarrierText")
                            .get(it.thisObject) as TextView
                    dateView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    clockView.setTypeface(Typeface.DEFAULT)
                    clockView.fontFeatureSettings = ""
                    carrierTextView.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }

        }
    }
}