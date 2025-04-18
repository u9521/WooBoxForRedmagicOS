package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeLauncher:MyFragment() {
    override val regKey: String
        get() = "scope_launcher"
    override val IData: InitView.ItemData.() -> Unit
        get() = {
            LsposedInactiveTip(this, mactivity!!).setViews()
            TitleText(textId = R.string.scope_launcher)
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.rm_zvoice_uninstalled_popupwindow,
                    tipsId = R.string.rm_zvoice_uninstalled_popupwindow_tips
                ), SwitchV("launcher_rm_zvoice_uninstall_dialog")
            )
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.launcher_Force_Support_ResizeActivity,
                    tipsId = R.string.launcher_Force_Support_ResizeActivity_tips
                ), SwitchV("launcher_Force_Support_ResizeActivity")
            )
        }
}