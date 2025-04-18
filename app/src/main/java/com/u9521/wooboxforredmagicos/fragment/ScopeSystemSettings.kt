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

object ScopeSystemSettings : MyFragment() {
    override val regKey: String
        get() = "scope_systemSettings"
    override val IData: InitView.ItemData.() -> Unit
        get() = {
            LsposedInactiveTip(this, mactivity!!).setViews()
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
            SwitchGroupBuilder(
                this,
                mactivity!!,
                SwitchGroupConfig(
                    "setting_Fun_override_maxyear_switch",
                    R.string.setting_Fun_override_maxyear_switch,
                    listOf(SubItemConfig.Arrow(R.string.setting_Fun_override_maxyear) {
                        InputDialogBuilder(
                            this, mactivity!!, InputDialogConfig(
                                R.string.setting_Fun_override_maxyear,
                                R.string.setting_Fun_override_maxyear_tips,
                                "fun_override_max_year",
                                2099,
                                PrefsTypeAdapter.INT,
                            )
                        ).show { Toast.makeText(mactivity, "设置成功", Toast.LENGTH_SHORT).show() }
                    })
                )
            ).build()
        }
}