package com.u9521.wooboxforredmagicos.fragment

import android.widget.Toast
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.InputDialogBuilder
import com.u9521.wooboxforredmagicos.compose.InputDialogConfig
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip
import com.u9521.wooboxforredmagicos.compose.PrefsTypeAdapter
import com.u9521.wooboxforredmagicos.compose.SubItemConfig
import com.u9521.wooboxforredmagicos.compose.SwitchGroupBuilder
import com.u9521.wooboxforredmagicos.compose.SwitchGroupConfig

object ScopeAndroid : MyFragment() {
    override val regKey: String = "scope_android"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "disable_flag_secure",
                R.string.disable_flag_secure,
                R.string.disable_flag_secure_summary,
                SubItemConfig.Switch(
                    "disable_flag_secure_enhanced",
                    R.string.disable_flag_secure_enhanced,
                    R.string.disable_flag_secure_enhanced_tips
                )
            )
        ).build()
        Line()
        TitleText(textId = R.string.corepacth)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.downgr, tipsId = R.string.downgr_summary
            ), SwitchV("downgrade")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.authcreak, tipsId = R.string.authcreak_summary
            ), SwitchV("authcreak")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.digestCreak, tipsId = R.string.digestCreak_summary
            ), SwitchV("digestCreak")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.UsePreSig, tipsId = R.string.UsePreSig_summary
            ), SwitchV("UsePreSig")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.enhancedMode, tipsId = R.string.enhancedMode_summary
            ), SwitchV("enhancedMode")
        )
        Line()
        TitleText(textId = R.string.notification)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.remove_alert_windows_notification,
                tipsId = R.string.remove_alert_windows_notification_tips
            ), SwitchV("remove_alert_windows_notification")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.allow_untrusted_touches,
                tipsId = R.string.allow_untrusted_touches_tips
            ), SwitchV("allow_untrusted_touches")
        )
        Line()
        //音量阶数
        TitleText(textId = R.string.sound)
        // alarm
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "alarm_volume_steps_switch",
                R.string.alarm_volume_steps_switch,
                R.string.media_volume_steps_summary,
                SubItemConfig.SeekBar(
                    "alarm_volume_steps",
                    3,
                    50,
                    15
                )
            )
        ).build()

        // media
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "media_volume_steps_switch",
                R.string.media_volume_steps_switch,
                R.string.media_volume_steps_summary,
                SubItemConfig.SeekBar(
                    "media_volume_steps",
                    3,
                    50,
                    15
                )
            )
        ).build()

        // notify
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "notify_volume_steps_switch",
                R.string.notify_volume_steps_switch,
                R.string.media_volume_steps_summary,
                SubItemConfig.SeekBar(
                    "notify_volume_steps",
                    3,
                    50,
                    15
                )
            )
        ).build()

        // ring
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "ring_volume_steps_switch",
                R.string.ring_volume_steps_switch,
                R.string.media_volume_steps_summary,
                SubItemConfig.SeekBar(
                    "ring_volume_steps",
                    3,
                    50,
                    15
                )
            )
        ).build()

        //voice call
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "vc_call_volume_steps_switch",
                R.string.vc_call_volume_steps_switch,
                R.string.media_volume_steps_summary,
                SubItemConfig.SeekBar(
                    "vc_call_volume_steps",
                    3,
                    50,
                    10
                )
            )
        ).build()
        Line()
        TitleText(textId = R.string.windowreply)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.rm_window_reply_app_restriction),
            SwitchV("rm_window_reply_restriction")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.rm_window_reply_count_restriction,
                tipsId = R.string.rm_window_reply_count_restriction_tips
            ),
            SwitchV("rm_window_reply_count_restriction")
        )
        Line()
        TitleText(textId = R.string.airplane_mode)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.airplane_mode_keep_bt,
                tipsId = R.string.airplane_mode_keep_bt_tips
            ),
            SwitchV("airplane_mode_keep_bt")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.airplane_mode_keep_wlan,
                tipsId = R.string.airplane_mode_keep_wlan_tips
            ),
            SwitchV("airplane_mode_keep_wlan")
        )
        TitleText(textId = R.string.wlan_server)
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "custom_wlan_countrycode", R.string.telecom_wlan_cc, R.string.telecom_wlan_cc_tips,
                SubItemConfig.Arrow(R.string.telecom_wlan_cc_set) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.telecom_wlan_cc_set,
                            R.string.telecom_wlan_cc_tips,
                            "telecom_wlan_cc_val",
                            "us",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "pin_sta_mac_sw", R.string.pin_sta_mac, R.string.pin_sta_mac_tips,
                SubItemConfig.Arrow(R.string.pin_sta_mac_set) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.pin_sta_mac_set,
                            R.string.pin_sta_mac_tips,
                            "pin_sta_mac",
                            "66:31:32:35:39:75",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "pin_ap_bssid_sw", R.string.pin_ap_bssid, R.string.pin_ap_bssid_tips,
                SubItemConfig.Arrow(R.string.pin_sta_mac_set) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.pin_sta_mac_set,
                            R.string.pin_ap_bssid_tips,
                            "pin_ap_bssid",
                            "b4:f3:cb:a7:b8:e7",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()

        Line()
        TitleText(textId = R.string.debug_option)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.block_system_server_log,
                tipsId = R.string.block_system_server_log_tips
            ),
            SwitchV("block_system_server_log")
        )
    }
}