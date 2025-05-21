package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeOther : MyFragment() {
    override val regKey = "scope_other"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        TitleText(textId = R.string.scope_nfcService)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.mute_nfc_sound),
            SwitchV("mute_nfc_sound")
        )
        Line()
        TitleText(textId = R.string.scope_permission_controller)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.allow_thirdparty_launcher, tipsId = R.string.allow_thirdparty_launcher_tips),
            SwitchV("allow_thirdparty_launcher")
        )
        Line()
        TitleText(textId = R.string.debug_option)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.debug_log, tipsId = R.string.debug_log_tips),
            SwitchV("debug_log", defValue = BuildConfig.DEBUG)
        )
    }
}
