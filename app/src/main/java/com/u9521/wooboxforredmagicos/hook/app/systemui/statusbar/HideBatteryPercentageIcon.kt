package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.widget.TextView
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideBatteryPercentageIcon : HookRegister() {
    override fun init() = hasEnable("hide_battery_percentage_icon") {
        val mFVBVLayout = ClassUtils.loadClass("com.zte.mifavor.views.MFVBatteryViewLayout")
        MethodFinder.fromClass(mFVBVLayout).filterByName("updateBatteryLevelText").first()
            .createAfterHook {
                val mCurrentUsedBatteryLevelView =
                    it.thisObject.javaClass.getDeclaredField("mCurrentUsedBatteryLevelView")
                        .get(it.thisObject) as TextView
                val mBateryLevel = it.thisObject.javaClass.getDeclaredField("mBateryLevel")
                    .getInt(it.thisObject)
                mCurrentUsedBatteryLevelView.text = mBateryLevel.toString()
            }
    }
}