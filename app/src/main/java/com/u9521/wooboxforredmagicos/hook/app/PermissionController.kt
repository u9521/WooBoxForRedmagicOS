package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.permissioncontroller.AllowThirdpartyLauncher
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object PermissionController : AppRegister() {
    override val packageName: List<String> = listOf("com.android.permissioncontroller")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox-PermissionController"
    override val loadDexkit: Boolean = true
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(
            lpparam,
            AllowThirdpartyLauncher, //允许设置第三方桌面
        )
    }
}