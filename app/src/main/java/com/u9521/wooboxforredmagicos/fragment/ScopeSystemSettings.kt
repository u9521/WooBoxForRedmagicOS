package com.u9521.wooboxforredmagicos.fragment

import android.view.View
import android.widget.Switch
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeSystemSettings : MyFragment() {
    override val regKey: String
        get() = "scope_systemSettings"
    override val IData: InitView.ItemData.() -> Unit
        get() = {
            LsposedInactiveTip.getTextSumV(mactivity!!)?.let { TextSummaryArrow(it) }
            TitleText(textId = R.string.scope_systemSettings)
            TextSummaryWithSwitch(
                TextSummaryV(textId = R.string.usb_install_switch_skip_verify),
                SwitchV("usb_install_switch_skip_verify")
            )
            TextSummaryWithSwitch(
                TextSummaryV(textId = R.string.setting_Froce_Display_ZvioceSwitch),
                SwitchV("setting_Froce_Display_ZvioceSwitch")
            )
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.setting_Froce_Display_WakeupGoogleAssistSwitch,
                    tipsId = R.string.setting_Froce_Display_WakeupGoogleAssistSwitch_tips
                ), SwitchV("setting_Froce_Display_WakeupGoogleAssistSwitch")
            )
            Line()
            TitleText(textId = R.string.Fun_options)
            val customMaxyearBinding = GetDataBinding(
                defValue = {
                    mactivity!!.getSP()!!.getBoolean("setting_Fun_override_maxyear_switch", false)
                }) { view, flags, data ->
                when (flags) {
                    1 -> (view as Switch).isEnabled = data as Boolean
                    2 -> view.visibility = if (data as Boolean) View.VISIBLE else View.GONE
                }
            }
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.setting_Fun_override_maxyear_switch,
                    tipsId = R.string.setting_Fun_override_maxyear_tips
                ), SwitchV(
                    "setting_Fun_override_maxyear_switch",
                    dataBindingSend = customMaxyearBinding.bindingSend
                )
            )
            TextSummaryArrow(
                dataBindingRecv = customMaxyearBinding.getRecv(2),
                textSummaryV = TextSummaryV(
                    textId = R.string.setting_Fun_override_maxyear,
                    onClickListener = {
                        MIUIDialog(mactivity!!) {
                            setTitle(R.string.setting_Fun_override_maxyear)
                            setMessage(R.string.setting_Fun_override_maxyear_tips)
                            var maxyear =
                                mactivity!!.getSP()!!.getInt("fun_override_max_year", 2099)
                                    .toString()
                            setEditText(maxyear, "最大年份数字", editCallBacks = {
                                maxyear = it
                            })
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                var maxyearInt = maxyear.toIntOrNull()
                                if (maxyearInt == null) {
                                    MIUIDialog(mactivity!!) {
                                        setCancelable(false)
                                        setTitle(R.string.format_error)
                                        setMessage(R.string.format_int_error)
                                        setRButton(R.string.Done) {
                                            dismiss()
                                        }
                                    }.show()
                                    dismiss()
                                    return@setRButton
                                }
                                mactivity!!.getSP()!!.edit()
                                    .putInt("fun_override_max_year", maxyearInt)
                                    .apply()
                                dismiss()
                            }
                        }.show()
                    }),
            )
        }
}