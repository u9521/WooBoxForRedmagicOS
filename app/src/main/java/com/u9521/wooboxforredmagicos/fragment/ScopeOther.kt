package com.u9521.wooboxforredmagicos.fragment

import android.widget.Toast
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.*

object ScopeOther : MyFragment() {
    override val regKey = "scope_other"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        TitleText(textId = R.string.mtp_service)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.hide_mtp_category_browse,
                tipsId = R.string.hide_mtp_category_browse_tips
            ), SwitchV("hide_mtp_category_browse")
        )
        SwitchGroupBuilder(
            this, mactivity!!, SwitchGroupConfig(
                "rename_mtp_storage_root_name_sw",
                R.string.rename_mtp_storage_root_name,
                R.string.rename_mtp_storage_root_name_tips,
                SubItemConfig.Arrow(R.string.mtp_storage_root_name) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mtp_storage_root_name,
                            R.string.rename_mtp_storage_root_name_tips,
                            "rename_mtp_storage_root_name",
                            "内部存储设备",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()

        Line()
        TitleText(textId = R.string.scope_nfcService)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.mute_nfc_sound),
            SwitchV("mute_nfc_sound")
        )
        Line()
        TitleText(textId = R.string.scope_permission_controller)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.allow_thirdparty_launcher,
                tipsId = R.string.allow_thirdparty_launcher_tips
            ),
            SwitchV("allow_thirdparty_launcher")
        )
        Line()
        TitleText(textId = R.string.scope_double_app)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.rm_double_low_memory_limit,
                tipsId = R.string.rm_double_low_memory_limit_tips
            ),
            SwitchV("rm_double_low_memory_limit")
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.double_any_app,
                tipsId = R.string.double_any_app_tips
            ),
            SwitchV("double_any_app")
        )
        Line()
        TitleText(textId = R.string.debug_option)
        TextSummaryWithSwitch(
            TextSummaryV(textId = R.string.debug_log, tipsId = R.string.debug_log_tips),
            SwitchV("debug_log", defValue = BuildConfig.DEBUG)
        )
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.block_system_server_log,
                tipsId = R.string.block_system_server_log_tips
            ),
            SwitchV("block_system_server_log")
        )
    }
}
