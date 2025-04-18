package com.u9521.wooboxforredmagicos.fragment

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.DataConfig
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip
import com.u9521.wooboxforredmagicos.compose.SwitchWithSeekbar

object ScopeAndroid : MyFragment() {
    override val regKey: String = "scope_android"
    override val IData: InitView.ItemData.() -> Unit = {
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
                tipsId = R.string.take_effect_after_reboot
            ), SwitchV("allow_untrusted_touches")
        )
        Line()
        //音量阶数
        TitleText(textId = R.string.sound)
        // alarm
        SwitchWithSeekbar(
            this, mactivity!!,
            DataConfig(
                "alarm_volume_steps_switch",
                "alarm_volume_steps",
                R.string.alarm_volume_steps_switch,
                R.string.media_volume_steps_summary,
                3,
                50,
                15
            )
        ).build()
        // media
        SwitchWithSeekbar(
            this, mactivity!!,
            DataConfig(
                "media_volume_steps_switch",
                "media_volume_steps",
                R.string.media_volume_steps_switch,
                R.string.media_volume_steps_summary,
                3,
                50,
                15
            )
        ).build()
        // notify
        SwitchWithSeekbar(
            this, mactivity!!,
            DataConfig(
                "notify_volume_steps_switch",
                "notify_volume_steps",
                R.string.notify_volume_steps_switch,
                R.string.media_volume_steps_summary,
                3,
                50,
                15
            )
        ).build()
        // ring
        SwitchWithSeekbar(
            this, mactivity!!,
            DataConfig(
                "ring_volume_steps_switch",
                "ring_volume_steps",
                R.string.ring_volume_steps_switch,
                R.string.media_volume_steps_summary,
                3,
                50,
                15
            )
        ).build()
        //voice call
        SwitchWithSeekbar(
            this, mactivity!!,
            DataConfig(
                "vc_call_volume_steps_switch",
                "vc_call_volume_steps",
                R.string.vc_call_volume_steps_switch,
                R.string.media_volume_steps_summary,
                3,
                50,
                10
            )
        ).build()
    }
}