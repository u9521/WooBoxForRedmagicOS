package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RMzvoiceUninstalDialog : HookRegister() {
    private val uHandle = ClassUtils.loadClass("android.os.UserHandle")
    private val zviocePackageName = "com.zte.halo.app"
    override fun init() = hasEnable("launcher_rm_zvoice_uninstall_dialog") {
        MethodFinder.fromClass("android.content.pm.LauncherApps").filterByName("isPackageEnabled")
            .filterByParamTypes(
                String::class.java,
                uHandle
            ).first().createBeforeHook {
                if (it.args[0].equals(zviocePackageName)) {
                    it.result = true
                }
            }
    }


}