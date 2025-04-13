package com.u9521.wooboxforredmagicos.fragment

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeSystemUI : MyFragment() {
    override val regKey: String = "scope_systemui"
    override val IData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip.getTextSumV(mactivity!!)
            ?.let { TextSummaryArrow(it) }
        TitleText(textId = R.string.statusbar)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.double_tap_to_sleep
            ), SwitchV("status_bar_double_tap_to_sleep")
        )
        Line()
        TitleText(textId = R.string.status_bar_clock_format)
        val customClockBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("custom_clock_switch", false)
            }) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextWithSwitch(
            TextV(textId = R.string.custom_clock_switch, colorId = R.color.purple_700),
            SwitchV(
                "custom_clock_switch", dataBindingSend = customClockBinding.bindingSend
            )
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_year),
            SwitchV("status_bar_time_year"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_month),
            SwitchV("status_bar_time_month"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_day),
            SwitchV("status_bar_time_day"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_week),
            SwitchV("status_bar_time_week"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_double_hour),
            SwitchV("status_bar_time_double_hour"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_period),
            SwitchV("status_bar_time_period", true),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_seconds),
            SwitchV("status_bar_time_seconds"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_hide_space),
            SwitchV("status_bar_time_hide_space"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_double_line),
            SwitchV("status_bar_time_double_line"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.status_bar_time_double_line_center_align),
            SwitchV("status_bar_time_double_line_center_align"),
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.status_bar_clock_size,
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "status_bar_clock_size",
            0,
            18,
            0,
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.status_bar_clock_double_line_size,
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "status_bar_clock_double_line_size",
            0,
            9,
            0,
            dataBindingRecv = customClockBinding.binding.getRecv(2)
        )
        TextWithSwitch(
            TextV(textId = R.string.dropdown_status_bar_clock_display_seconds),
            SwitchV("dropdown_status_bar_clock_display_seconds")
        )
        TextWithSwitch(
            TextV(textId = R.string.remove_dropdown_status_bar_clock_redone),
            SwitchV("remove_dropdown_status_bar_clock_redone")
        )
        Line()
        TitleText(textId = R.string.status_bar_icon)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.hide_prompt_view,
                tipsId = R.string.hide_prompt_view_summary
            ), SwitchV("hide_prompt_view")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.hide_battery_percentage_icon,
                tipsId = R.string.hide_battery_percentage_icon_summary
            ), SwitchV("hide_battery_percentage_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_wifi_activity_icon),
            SwitchV("hide_wifi_activity_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_mobile_activity_icon),
            SwitchV("hide_mobile_activity_icon")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.hide_vpn_icon, tipsId = R.string.hide_vpn_icon_tips
            ), SwitchV("hide_vpn_icon")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.status_bar_bluetooth_icon_acts_global,
                tipsId = R.string.status_bar_bluetooth_icon_acts_global_tips
            ), SwitchV("status_bar_bluetooth_icon_acts_global")
        )
        Line()
        TitleText(textId = R.string.status_bar_network_speed)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.status_bar_network_speed_refresh_speed,
                tipsId = R.string.status_bar_network_speed_refresh_speed_summary
            ), SwitchV("status_bar_network_speed_refresh_speed")
        )
        val statusBarDualRowNetworkSpeedBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("status_bar_dual_row_network_speed", false)
            }

        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.status_bar_dual_row_network_speed,
                tipsId = R.string.status_bar_dual_row_network_speed_summary
            ), SwitchV(
                "status_bar_dual_row_network_speed",
                dataBindingSend = statusBarDualRowNetworkSpeedBinding.bindingSend
            )
        )
        Text(
            textId = R.string.status_bar_network_speed_dual_row_size,
            dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "status_bar_network_speed_dual_row_size",
            6,
            8,
            6,
            dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.status_bar_network_speed_dual_row_width,
            dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "status_bar_network_speed_dual_row_width",
            28,
            38,
            35,
            dataBindingRecv = statusBarDualRowNetworkSpeedBinding.binding.getRecv(2)
        )
        Line()
        TitleText(textId = R.string.notification)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_finished_charging,
            ), SwitchV("remove_finished_charging")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_usb_debugging,
            ), SwitchV("remove_usb_debugging")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_dev_mode_is_on,
            ), SwitchV("remove_dev_mode_is_on")
        )
        Line()
        TitleText(textId = R.string.features)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.enable_charging_ripple,
            ), SwitchV("enable_charging_ripple")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.gesture_use_default_digital_assist,
                tipsId = R.string.gesture_use_default_digital_assist_tips
            ), SwitchV("gesture_use_default_digital_assist")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.no_vibrate_volKey_Long_press,
                tipsId = R.string.no_vibrate_volKey_Long_press_tips
            ), SwitchV("no_vibrate_volKey_Long_press")
        )
        Line()
        TitleText(textId = R.string.lockscreen)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_red_one,
            ), SwitchV("remove_red_one")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.lockscreen_allow_adjust_volume,
                tipsId = R.string.lockscreen_allow_adjust_volume_tips
            ), SwitchV("lockscreen_allow_adjust_volume")
        )
        Line()
        TitleText(textId = R.string.quick_settings_panel)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_footer_security_warn,
            ), SwitchV("remove_footer_security_warn")
        )
        val oldQSCustomSwitchBinding = GetDataBinding(
            defValue = { mactivity!!.getSP()!!.getBoolean("qs_custom_switch", false) }

        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.qs_custom_switch,
                tipsId = R.string.qs_custom_switch_summary,
                colorId = R.color.purple_700
            ), SwitchV(
                "qs_custom_switch", dataBindingSend = oldQSCustomSwitchBinding.bindingSend
            )
        )
        Text(
            textId = R.string.qs_custom_rows,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "qs_custom_rows",
            1,
            9,
            4,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.qs_custom_rows_horizontal,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "qs_custom_rows_horizontal",
            1,
            4,
            2,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.qs_custom_columns,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "qs_custom_columns",
            1,
            9,
            4,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        Text(
            textId = R.string.qs_custom_columns_unexpanded,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )
        SeekBarWithText(
            "qs_custom_columns_unexpanded",
            1,
            10,
            6,
            dataBindingRecv = oldQSCustomSwitchBinding.binding.getRecv(2)
        )


    }
}