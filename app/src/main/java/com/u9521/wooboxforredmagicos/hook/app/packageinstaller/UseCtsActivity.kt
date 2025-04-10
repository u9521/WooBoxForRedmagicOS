package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object UseCtsActivity : HookRegister() {
    override fun init() = hasEnable("installer_cts_mode") {
        MethodFinder.fromClass("com.android.packageinstaller.InstallStart")
            .filterByName("getCallingPackageNameForUid").filterByParamTypes(Int::class.java).first()
            .createBeforeHook {
                it.result = "hello.cts"
            }
    }
}