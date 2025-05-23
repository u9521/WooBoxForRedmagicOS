package com.u9521.wooboxforredmagicos.fragment

import android.widget.Toast
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.activity.view.TextV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.*

object ScopeSystemUI : MyFragment() {
    override val regKey: String = "scope_systemui"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        TitleText(textId = R.string.statusbar)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.double_tap_to_sleep
            ), SwitchV("status_bar_double_tap_to_sleep")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.statusbar_font_restore
            ), SwitchV("statusbar_font_restore")
        )
        SwitchGroupBuilder(
            this, mactivity!!,
            SwitchGroupConfig(
                "use_aosp_notify",
                R.string.use_apps_notify_icon,
                R.string.aosp_notify_tips,
                SubItemConfig.SeekBar(
                    "aosp_icon_size_dp",
                    0,
                    64,
                    16
                )
            )
        ).build()

        Line()
        TitleText(textId = R.string.status_bar_clock_format)
//        SwitchGroupBuilder(
//            this, mactivity!!, SwitchGroupConfig(
//                "custom_clock_switch", R.string.custom_clock_switch, listOf(
//                    SubItemConfig.Switch(
//                        "status_bar_time_year", R.string.status_bar_time_year
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_month", R.string.status_bar_time_month
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_day", R.string.status_bar_time_day
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_week", R.string.status_bar_time_week
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_double_hour", R.string.status_bar_time_double_hour
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_period", R.string.status_bar_time_period
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_seconds", R.string.status_bar_time_seconds
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_hide_space", R.string.status_bar_time_hide_space
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_double_line", R.string.status_bar_time_double_line
//                    ),
//                    SubItemConfig.Switch(
//                        "status_bar_time_double_line_center_align",
//                        R.string.status_bar_time_double_line_center_align
//                    ),
//                    SubItemConfig.Text(
//                        R.string.status_bar_clock_size
//                    ),
//                    SubItemConfig.SeekBar(
//                        "status_bar_clock_size",
//                        0,
//                        18,
//                        0
//                    ),
//                    SubItemConfig.Text(
//                        R.string.status_bar_clock_double_line_size,
//                    ),
//                    SubItemConfig.SeekBar(
//                        "status_bar_clock_double_line_size",
//                        0,
//                        9,
//                        0
//                    ),
//                )
//            )
//        ).build()
        TextWithSwitch(
            TextV(textId = R.string.dropdown_status_bar_clock_display_seconds),
            SwitchV("collapsed_status_bar_clock_display_seconds")
        )
        Line()
        TitleText(textId = R.string.status_bar_icon)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.align_fan_refreshrate_aosp,
                tipsId = R.string.align_fan_refreshrate_aosp_tip
            ), SwitchV("align_fan_refreshrate_aosp")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.hide_battery_percentage_icon,
                tipsId = R.string.hide_battery_percentage_icon_summary
            ), SwitchV("hide_battery_percentage_icon")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.show_charge_indicator_on_power_plugin,
                tipsId = R.string.show_charge_indicator_on_power_plugin_tips
            ), SwitchV("show_charge_indicator_on_power_plugin")
        )
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "battery_icon_fine_tune",
                R.string.battery_icon_fine_tune,
                R.string.battery_icon_fine_tune_tips,
                listOf(
                    SubItemConfig.Switch(
                        "show_battery_meter",
                        R.string.show_battery_meter,
                        R.string.show_battery_meter_tips
                    ),
                    SubItemConfig.Switch(
                        "show_charge_indicator_inside",
                        R.string.show_charge_indicator_inside,
                        textTipsResId = R.string.show_charge_indicator_inside_tips
                    ),
                    SubItemConfig.Switch(
                        "show_charge_indicator_outside",
                        R.string.show_charge_indicator_outside
                    ),
                )
            )
        ).build()
        TextWithSwitch(
            TextV(textId = R.string.hide_wifi_activity_icon),
            SwitchV("hide_wifi_activity_icon")
        )
        TextWithSwitch(
            TextV(textId = R.string.hide_mobile_activity_icon), SwitchV("hide_mobile_activity_icon")
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
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "status_bar_dual_row_network_speed",
                R.string.status_bar_dual_row_network_speed,
                R.string.status_bar_dual_row_network_speed_summary,
                listOf(
                    SubItemConfig.Switch("speed_unit_hide_per_second",R.string.speed_unit_hide_per_second),

                    SubItemConfig.Text(R.string.status_bar_network_speed_dual_row_size),
                    SubItemConfig.SeekBar(
                        "status_bar_network_speed_dual_row_size",
                        6,
                        8,
                        6
                    ),
                    SubItemConfig.Text(R.string.status_bar_network_speed_dual_row_width),
                    SubItemConfig.SeekBar(
                        "status_bar_network_speed_dual_row_width",
                        28,
                        39,
                        35
                    ),
                    SubItemConfig.Text(R.string.status_bar_network_speed_dual_row_digit_len),
                    SubItemConfig.SeekBar(
                        "status_bar_network_speed_dual_row_digit_len",
                        0,
                        3,
                        3
                    ),
                    SubItemConfig.Text(R.string.low_speed_hide_kilo_bytes),
                    SubItemConfig.SeekBar(
                        "low_speed_hide_kilo_bytes",
                        -1,
                        10,
                        -1
                    )

                )
            )
        ).build()
//        Line()
//        TitleText(textId = R.string.notification)

        Line()
        TitleText(textId = R.string.features)
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
        SwitchGroupBuilder(
            this, mactivity!!,
            SwitchGroupConfig(
                "aosp_singlehandmode_adjust",
                R.string.aosp_singlehandmode_adjust,
                SubItemConfig.Arrow(
                    R.string.aosp_singlehandmode_offest
                ) {
                    InputDialogBuilder(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.input_int,
                            R.string.aosp_singlehandmode_offest,
                            "aosp_singlehandmode_offest", 0, PrefsTypeAdapter.INT
                        )
                    ).show {
                        Toast.makeText(mactivity, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                }
            )
        ).build()

        Line()
        TitleText(textId = R.string.lockscreen)
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
                textId = R.string.qs_shortcut_redir_calendar,
            ), SwitchV("qs_header_shortcut_redirect_calendar")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.qs_shortcut_redir_search,
            ), SwitchV("qs_header_shortcut_redirect_search")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.qs_show_carrier,
            ), SwitchV("qs_show_carrier")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.qs_show_search,
            ), SwitchV("qs_show_search")
        )

        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "qs_custom_switch",
                R.string.qs_custom_switch,
                R.string.qs_custom_switch_summary,
                listOf(
                    SubItemConfig.Text(R.string.qs_custom_rows),
                    SubItemConfig.SeekBar("qs_custom_rows", 1, 9, 3),

                    SubItemConfig.Text(R.string.qs_custom_rows_landscape),
                    SubItemConfig.SeekBar("qs_custom_rows_landscape", 1, 4, 1),

                    SubItemConfig.Text(R.string.qs_custom_columns),
                    SubItemConfig.SeekBar("qs_custom_columns", 1, 9, 5),

                    SubItemConfig.Text(R.string.qs_custom_columns_landscape),
                    SubItemConfig.SeekBar("qs_custom_columns_landscape", 1, 10, 7),

                    SubItemConfig.TextSummary(
                        R.string.qs_custom_columns_editor,
                        R.string.qs_custom_columns_original_tips
                    ),
                    SubItemConfig.SeekBar("qs_custom_columns_editor", 1, 9, 4),

                    SubItemConfig.TextSummary(
                        R.string.qs_custom_columns_landscape_original,
                        R.string.qs_custom_columns_landscape_original_tips
                    ),
                    SubItemConfig.SeekBar("qs_custom_columns_landscape_editor", 1, 10, 6),


                    )
            )
        ).build()

    }
}