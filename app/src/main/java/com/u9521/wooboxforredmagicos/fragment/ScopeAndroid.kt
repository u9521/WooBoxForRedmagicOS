package com.u9521.wooboxforredmagicos.fragment

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeAndroid : MyFragment() {
    override val regKey: String = "scope_android"
    override val IData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip.getTextSumV(mactivity!!)?.let { TextSummaryArrow(it) }
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
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.disable_flag_secure,
                tipsId = R.string.disable_flag_secure_summary
            ), SwitchV("disable_flag_secure")
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
        TitleText(textId = R.string.sound)
        // alarm
        val alarmVolumeStepsSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("alarm_volume_steps_switch", false)
            }
        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.alarm_volume_steps_switch,
                tips = "${mactivity!!.getString(R.string.take_effect_after_reboot)}\n${
                    mactivity!!.getString(
                        R.string.media_volume_steps_summary
                    )
                }"
            ), SwitchV(
                "alarm_volume_steps_switch",
                dataBindingSend = alarmVolumeStepsSwitchBinding.bindingSend
            )
        )
        SeekBarWithText(
            "alarm_volume_steps",
            3,
            50,
            15,
            dataBindingRecv = alarmVolumeStepsSwitchBinding.binding.getRecv(2)
        )
        // media
        val mediaVolumeStepsSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("media_volume_steps_switch", false)
            }

        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.media_volume_steps_switch,
                tips = "${mactivity!!.getString(R.string.take_effect_after_reboot)}\n${
                    mactivity!!.getString(
                        R.string.media_volume_steps_summary
                    )
                }"
            ), SwitchV(
                "media_volume_steps_switch",
                dataBindingSend = mediaVolumeStepsSwitchBinding.bindingSend
            )
        )
        SeekBarWithText(
            "media_volume_steps",
            3,
            50,
            15,
            dataBindingRecv = mediaVolumeStepsSwitchBinding.binding.getRecv(2)
        )
        // notify
        val notifyVolumeStepsSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("notify_volume_steps_switch", false)
            }

        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.notify_volume_steps_switch,
                tips = "${mactivity!!.getString(R.string.take_effect_after_reboot)}\n${
                    mactivity!!.getString(
                        R.string.media_volume_steps_summary
                    )
                }"
            ), SwitchV(
                "notify_volume_steps_switch",
                dataBindingSend = notifyVolumeStepsSwitchBinding.bindingSend
            )
        )
        SeekBarWithText(
            "notify_volume_steps",
            3,
            50,
            15,
            dataBindingRecv = notifyVolumeStepsSwitchBinding.binding.getRecv(2)
        )
        // ring
        val ringVolumeStepsSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("ring_volume_steps_switch", false)
            }
        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.ring_volume_steps_switch,
                tips = "${mactivity!!.getString(R.string.take_effect_after_reboot)}\n${
                    mactivity!!.getString(
                        R.string.media_volume_steps_summary
                    )
                }"
            ), SwitchV(
                "ring_volume_steps_switch",
                dataBindingSend = ringVolumeStepsSwitchBinding.bindingSend
            )
        )
        SeekBarWithText(
            "ring_volume_steps",
            3,
            50,
            15,
            dataBindingRecv = ringVolumeStepsSwitchBinding.binding.getRecv(2)
        )
        //voice call
        val vc_callVolumeStepsSwitchBinding = GetDataBinding(
            defValue = {
                mactivity!!.getSP()!!.getBoolean("vc_call_volume_steps_switch", false)
            }

        ) { view, flags, data ->
            when (flags) {
                1 -> (view as Switch).isEnabled = data as Boolean
                2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
            }
        }
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.vc_call_volume_steps_switch,
                tips = "${mactivity!!.getString(R.string.take_effect_after_reboot)}\n${
                    mactivity!!.getString(
                        R.string.media_volume_steps_summary
                    )
                }"
            ), SwitchV(
                "vc_call_volume_steps_switch",
                dataBindingSend = vc_callVolumeStepsSwitchBinding.bindingSend
            )
        )
        SeekBarWithText(
            "vc_call_volume_steps",
            3,
            50,
            10,
            dataBindingRecv = vc_callVolumeStepsSwitchBinding.binding.getRecv(2)
        )
    }
}