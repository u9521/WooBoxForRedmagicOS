package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.settings.ForceShowGAShortcutSwitch
import com.u9521.wooboxforredmagicos.hook.app.settings.ForceShowVASwitch
import com.u9521.wooboxforredmagicos.hook.app.settings.FunSettingoverrideMAXyear
import com.u9521.wooboxforredmagicos.hook.app.settings.UsbInstallNoVerify
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Settings : AppRegister() {
    override val packageName: List<String> = listOf("com.android.settings")

    override val processName: List<String> = emptyList()

    override val logTag: String = "Woobox-Settings"
    override val loadDexkit: Boolean = false

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(
            lpparam,
            UsbInstallNoVerify, //取消usb安装账号验证
            ForceShowVASwitch,//强制在手势设置中显示手势打开智慧语言
            ForceShowGAShortcutSwitch,//强制显示长按电源建打开Google助理
            FunSettingoverrideMAXyear,//调整时间设置的最大年份
        )
    }

}