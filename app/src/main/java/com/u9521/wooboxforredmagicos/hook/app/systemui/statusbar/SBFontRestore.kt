package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.graphics.Typeface
import android.view.ViewGroup.LayoutParams
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object SBFontRestore : HookRegister() {
    override fun init() {
        hasEnable("statusbar_font_restore") {
            //clock
            MethodFinder.fromClass("com.android.systemui.statusbar.phone.PhoneStatusBarView")
                .filterByName("onFinishInflate").first()
                .createAfterHook {
                    val mClock = it.thisObject.javaClass.getDeclaredField("mClock")
                        .get(it.thisObject) as TextView
                    mClock.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mClock.fontFeatureSettings = ""
                    mClock.layoutParams.apply {
                        width = LayoutParams.WRAP_CONTENT
                        mClock.layoutParams = this
                    }
                }
            //network speed
            MethodFinder.fromClass("com.zte.feature.speed.StatusBarNetSpeedMFV")
                .filterByName("init").first().createAfterHook {
                    val mSpeedText = it.thisObject.javaClass.getDeclaredField("mSpeedText")
                        .get(it.thisObject) as TextView
                    val mSpeedUnit = it.thisObject.javaClass.getDeclaredField("mSpeedUnit")
                        .get(it.thisObject) as TextView
                    mSpeedText.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                    mSpeedUnit.setTypeface(Typeface.DEFAULT, Typeface.BOLD)
                }
            //battery percentage
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