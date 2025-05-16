package com.u9521.wooboxforredmagicos.activity

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import cn.fkj233.ui.activity.MIUIActivity
import cn.fkj233.ui.activity.data.InitView
import cn.fkj233.ui.activity.view.SwitchV
import cn.fkj233.ui.activity.view.TextSummaryV
import cn.fkj233.ui.dialog.MIUIDialog
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.R
import com.u9521.wooboxforredmagicos.compose.LsposedInactiveTip
import com.u9521.wooboxforredmagicos.fragment.AboutModule
import com.u9521.wooboxforredmagicos.fragment.ScopeAndroid
import com.u9521.wooboxforredmagicos.fragment.ScopeLauncher
import com.u9521.wooboxforredmagicos.fragment.ScopeOther
import com.u9521.wooboxforredmagicos.fragment.ScopePackageInstaller
import com.u9521.wooboxforredmagicos.fragment.ScopeSystemSettings
import com.u9521.wooboxforredmagicos.fragment.ScopeSystemUI
import com.u9521.wooboxforredmagicos.fragment.ScopeSystemUpdate
import com.u9521.wooboxforredmagicos.util.BackupAndRestore
import com.u9521.wooboxforredmagicos.util.ShellUtils
import kotlin.system.exitProcess

class SettingsActivity : MIUIActivity() {

    private val activity = this
    var lsposedLoaded = true
    var mInitvew: InitView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        checkLSPosed()
        super.onCreate(savedInstanceState)
    }

    //检测LSPosed是否激活
    @Suppress("DEPRECATION")
    @SuppressLint("WorldReadableFiles")
    private fun checkLSPosed() {
        try {
            setSP(getSharedPreferences("WooboxConfig", MODE_WORLD_READABLE))
        } catch (exception: SecurityException) {
            val viewonlySP = getSharedPreferences("viewonly", MODE_PRIVATE)
//            reset onstart
            viewonlySP.edit().clear().apply()
            lsposedLoaded = false
            setSP(viewonlySP)
            MIUIDialog(this) {
                setTitle(R.string.Tips)
                setMessage(R.string.not_support)
                setCancelable(false)
                setLButton(R.string.ignore) {
                    dismiss()
                }
                setRButton(R.string.Done) {
                    exitProcess(0)
                }
            }.show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        BackupAndRestore(this).handleActivityResult(requestCode, resultCode, data)
    }

    init {
        initView {
            mInitvew = this
            registerMain(getString(R.string.app_name), false) {
                LsposedInactiveTip(this, this@SettingsActivity).setViews()
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.main_switch, colorId = R.color.purple_700),
                    SwitchV("main_switch", true)
                )
                TextSummaryWithSwitch(
                    TextSummaryV(textId = R.string.HideLauncherIcon),
                    SwitchV("hLauncherIcon", onClickListener = {
                        packageManager.setComponentEnabledSetting(
                            ComponentName(activity, "${BuildConfig.APPLICATION_ID}.launcher"),
                            if (it) {
                                PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                            } else {
                                PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                            },
                            PackageManager.DONT_KILL_APP
                        )
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.matters_needing_attention,
                        colorId = R.color.red,
                        onClickListener = {
                            MIUIDialog(activity) {
                                setTitle(R.string.matters_needing_attention)
                                setMessage(
                                    R.string.matters_needing_attention_context
                                )
                                setRButton(R.string.Done) {
                                    dismiss()
                                }
                            }.show()
                        })
                )
                Line()
                TitleText(textId = R.string.scope)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemui,
                        tipsId = R.string.scope_systemui_summary,
                        onClickListener = { showFragment(ScopeSystemUI.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_android,
                        tipsId = R.string.scope_android_summary,
                        onClickListener = { showFragment(ScopeAndroid.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_packageinstaller,
                        onClickListener = { showFragment(ScopePackageInstaller.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_launcher,
                        onClickListener = { showFragment(ScopeLauncher.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_systemSettings,
                        onClickListener = { showFragment(ScopeSystemSettings.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_system_update,
                        onClickListener = { showFragment(ScopeSystemUpdate.regKey) })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.scope_other,
                        tipsId = R.string.scope_other_summary,
                        onClickListener = { showFragment(ScopeOther.regKey) })
                )
                Line()
                TitleText(textId = R.string.backup_restore)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.backup,
                        tipsId = R.string.backup_tips,
                        onClickListener = { BackupAndRestore(this@SettingsActivity).startBackup() })
                )
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.restore,
                        tipsId = R.string.restore_tips,
                        onClickListener = {
                            BackupAndRestore(this@SettingsActivity).confirmRestore(
                                this
                            )
                        })
                )
                Line()
                TitleText(textId = R.string.about)
                TextSummaryArrow(
                    TextSummaryV(
                        textId = R.string.about_module,
                        tips = getString(R.string.about_module_summary),
                        onClickListener = { showFragment(AboutModule.regKey) })
                )

            }
            AboutModule.registerView(
                this@SettingsActivity,
                getString(R.string.about_module),
                true
            )
            ScopeAndroid.registerView(
                this@SettingsActivity, getString(R.string.scope_android), false
            )
            ScopeLauncher.registerView(
                this@SettingsActivity, getString(R.string.scope_launcher), false
            )
            ScopeOther.registerView(
                this@SettingsActivity,
                getString(R.string.scope_other),
                false
            )
            ScopePackageInstaller.registerView(
                this@SettingsActivity, getString(R.string.scope_packageinstaller), false
            )
            ScopeSystemSettings.registerView(
                this@SettingsActivity, getString(R.string.scope_systemSettings), false
            )
            ScopeSystemUI.registerView(
                this@SettingsActivity, getString(R.string.scope_systemui), false
            )
            ScopeSystemUpdate.registerView(
                this@SettingsActivity, getString(R.string.scope_system_update), false
            )

            registerMenu(getString(R.string.menu)) {
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = arrayOf("reboot")
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
                TextSummaryArrow(
                    TextSummaryV(textId = R.string.reboot_host, onClickListener = {
                        MIUIDialog(activity) {
                            setTitle(R.string.Tips)
                            setMessage(R.string.are_you_sure_reboot_scope)
                            setLButton(R.string.cancel) {
                                dismiss()
                            }
                            setRButton(R.string.Done) {
                                val command = mutableListOf<String>()
                                resources.getStringArray(R.array.xposed_scope).forEach {
                                    if (it == "android") {
                                        return@forEach
                                    }
                                    command.add("killall $it")
                                }
                                ShellUtils.execCommand(command, true)
                                dismiss()
                            }
                        }.show()
                    })
                )
            }
        }
    }
}