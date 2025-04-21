package com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen

import android.graphics.Typeface
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object KSBFontRestore : HookRegister() {
    override fun init() {
        hasEnable("statusbar_font_restore") {
            val cCHVClazz =
                ClassUtils.loadClass("com.android.systemui.statusbar.phone.KeyguardStatusBarView")
            MethodFinder.fromClass(cCHVClazz).filterByName("onFinishInflate").first()
                .createAfterHook {
                    val carrierTextView =
                        it.thisObject.javaClass.getDeclaredField("mCarrierLabel")
                            .get(it.thisObject) as TextView
                    carrierTextView.setTypeface(Typeface.DEFAULT,Typeface.BOLD)
                }
        }
    }
}