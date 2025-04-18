package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopePackageInstaller : MyFragment() {
    override val regKey: String
        get() = "scope_packageinstaller"
    override val IData: InitView.ItemData.() -> Unit
        get() = {
            LsposedInactiveTip(this,mactivity!!).setViews()
            TitleText(textId = R.string.scope_packageinstaller)
            TextSummaryWithSwitch(
                TextSummaryV(textId = R.string.skip_apk_scan), SwitchV("skip_apk_scan")
            )
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.hide_purify_switch,
                    tipsId = R.string.hide_purify_switch_summery
                ), SwitchV("hide_purify_switch")
            )
            TextSummaryWithSwitch(
                TextSummaryV(textId = R.string.installer_hide_store_hint),
                SwitchV("installer_hide_store_hint")
            )
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.installer_cts_mode,
                    tipsId = R.string.installer_cts_mode_tips
                ), SwitchV("installer_cts_mode")
            )
        }
}
