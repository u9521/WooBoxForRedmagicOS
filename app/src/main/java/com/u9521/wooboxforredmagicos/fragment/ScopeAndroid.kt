package com.u9521.wooboxforredmagicos.fragment

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip
import com.u9521.wooboxforredmagicos.compose.SubItemConfig
import com.u9521.wooboxforredmagicos.compose.SwitchGroupBuilder
import com.u9521.wooboxforredmagicos.compose.SwitchGroupConfig

object ScopeAndroid : MyFragment() {
    override val regKey: String = "scope_android"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        val disableFlagSecureSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("disable_flag_secure", false)
            }
        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_flag_secure,
                tipsId = R.string.disable_flag_secure_summary
            ),
            SwitchV(
                "disable_flag_secure",
                dataBindingSend = disableFlagSecureSwitchBinding.bindingSend
            )
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_flag_secure_enhanced,
                tipsId = R.string.disable_flag_secure_enhanced_tips
            ),
            SwitchV("disable_flag_secure_enhanced"),
            dataBindingRecv = disableFlagSecureSwitchBinding.getRecv(2)
        )

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
        TitleText(textId = R.string.features)
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
    }
}