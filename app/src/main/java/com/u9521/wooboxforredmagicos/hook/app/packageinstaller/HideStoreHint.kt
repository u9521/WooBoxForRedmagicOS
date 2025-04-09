package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideStoreHint : HookRegister() {
    override fun init() = hasEnable("installer_hide_store_hint") {
        MethodFinder.fromClass("com.android.packageinstaller.PackageInstallerActivity")
            .filterByName("needShowOfficialRecommend").first()
            .createBeforeHook(block = {
                it.result = false
            })
    }
}