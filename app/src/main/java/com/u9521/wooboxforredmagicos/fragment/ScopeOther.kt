package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeOther : MyFragment() {
    override val regKey = "scope_other"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        TitleText(textId = R.string.scope_security_center)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.unlock_self_start_quantity),
            SwitchV("unlock_self_start_quantity")
        )
        Line()
        TitleText(textId = R.string.scope_alarmclock)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.remove_clock_widget_redone),
            SwitchV("remove_clock_widget_redone")
        )
        Line()
        TitleText(textId = R.string.scope_permission_controller)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.allow_thirdparty_launcher, tipsId = R.string.allow_thirdparty_launcher_tips),
            SwitchV("allow_thirdparty_launcher")
        )
    }
}
