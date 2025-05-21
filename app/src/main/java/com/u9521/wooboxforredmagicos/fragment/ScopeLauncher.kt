package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip

object ScopeLauncher:MyFragment() {
    override val regKey: String
        get() = "scope_launcher"
    override val iData: InitView.ItemData.() -> Unit
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
                    textId = R.string.launcher_force_support_resize_activity,
                    tipsId = R.string.launcher_force_support_resize_activity_tips
                ), SwitchV("launcher_force_support_resize_activity")
            )
            TextSummaryWithSwitch(
                TextSummaryV(
                    textId = R.string.launcher_force_show_blacklist_apps,
                    tipsId = R.string.launcher_force_show_blacklist_apps_tips
                ), SwitchV("launcher_force_show_blacklist_apps")
            )
        }
}