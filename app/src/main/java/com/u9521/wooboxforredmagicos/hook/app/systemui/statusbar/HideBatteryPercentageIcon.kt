package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XposedHelpers

object HideBatteryPercentageIcon : HookRegister() {
    override fun init() = hasEnable("hide_battery_percentage_icon") {
        return@hasEnable
//        findMethod("com.oplusos.systemui.statusbar.widget.StatBatteryMeterView") {
//            name == "updatePercentText"
//        }.hookAfter {
//            val textView =
//                XposedHelpers.getObjectField(it.thisObject, "batteryPercentText") as TextView
//            textView.text = (textView.text as String).replace("%", "")
//        }
    }
}