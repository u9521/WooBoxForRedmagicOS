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

object ScopeSystemUpdate : MyFragment() {
    override val regKey = "scope_system_update"
    override val iData: InitView.ItemData.() -> Unit = {
        LsposedInactiveTip(this, mactivity!!).setViews()
        TitleText(textId = R.string.system_update_mock_info)
        //model
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_model_sw", R.string.system_update_mock_model,
                SubItemConfig.Arrow(R.string.mock_model) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_model,
                            R.string.mock_model_tips,
                            "system_update_mock_model",
                            "NX769J",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //imei
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_imei_sw", R.string.system_update_mock_imei_sw,
                SubItemConfig.Arrow(R.string.mock_imei) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_imei,
                            R.string.mock_imei_tips,
                            "system_update_mock_imei",
                            "114514191981000",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //local
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_local_sw", R.string.system_update_mock_local,
                SubItemConfig.Arrow(R.string.mock_local) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_local,
                            R.string.mock_local_tips,
                            "system_update_mock_local",
                            "zh_CN",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //appSignatureMD5+appVersioncode
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_sign_sw", R.string.system_update_mock_sign,
                SubItemConfig.Arrow(R.string.mock_sign) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_sign,
                            R.string.mock_sign_tips,
                            "system_update_mock_sign",
                            "a2e44d1795185b8e3281444007effb7f_UNJ.REDMAGIC.FOTA.14.0.0.2409191549",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //build fingerprint
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_fingerprint_sw", R.string.system_update_mock_fingerprint,
                SubItemConfig.Arrow(R.string.mock_fingerprint) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_fingerprint,
                            R.string.mock_fingerprint_tips,
                            "system_update_mock_fingerprint",
                            "nubia/NX769J/NX769J:14/UKQ1.230917.001/20250310.231626:user/release-keys",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //system version
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_build_display_sw", R.string.system_update_mock_build_display,
                SubItemConfig.Arrow(R.string.mock_build_display) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_build_display,
                            R.string.mock_build_display_tips,
                            "system_update_mock_build_display",
                            "RedMagicOS9.5.25_NX769J_NY",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //inner version
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_system_inner_version_sw", R.string.system_update_mock_system_inner_version,
                SubItemConfig.Arrow(R.string.mock_system_inner_version) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_system_inner_version,
                            R.string.mock_system_inner_version_tips,
                            "system_update_mock_system_inner_version",
                            "GEN_CN_NX769SV1.5.0B25",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        //VariantId
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_variant_id_sw", R.string.system_update_mock_variant_id,
                SubItemConfig.Arrow(R.string.mock_variant_id) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_variant_id,
                            R.string.mock_variant_id_tips,
                            "system_update_mock_variant_id",
                            "GEN_NY_CN",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        // mock manufacture
        SwitchGroupBuilder(
            this,
            mactivity!!,
            SwitchGroupConfig(
                "system_update_mock_manufacture_sw", R.string.system_update_mock_manufacture,
                SubItemConfig.Arrow(R.string.mock_manufacture) {
                    InputDialogBuilder<String>(
                        this,
                        mactivity!!,
                        InputDialogConfig(
                            R.string.mock_manufacture,
                            R.string.mock_manufacture_tips,
                            "system_update_mock_manufacture",
                            "ZTE",
                            PrefsTypeAdapter.STRING
                        )
                    ).show {
                        Toast.makeText(mactivity!!, "设置成功", Toast.LENGTH_SHORT).show()
                        it.dismiss()
                    }
                })
        ).build()
        Line()
        TitleText(textId = R.string.system_update_block_update)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.system_update_block_update,
                tipsId = R.string.system_update_block_update_tips
            ),
            SwitchV("system_update_block_update")
        )
        Line()
        TitleText(textId = R.string.log_info)
        TextSummaryWithSwitch(
            TextSummaryV(
                textId = R.string.system_update_log_info,
                tipsId = R.string.system_update_log_info_tips
            ),
            SwitchV("system_update_log_info")
        )
    }
}
