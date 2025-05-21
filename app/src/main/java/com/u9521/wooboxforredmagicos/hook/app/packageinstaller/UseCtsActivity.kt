package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object UseCtsActivity : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("installer_cts_mode") {
        val callerNameMe =
            getDefaultCL().loadClass("com.android.packageinstaller.InstallStart")
                .getDeclaredMethod("getCallingPackageNameForUid", Int::class.javaPrimitiveType)
        val ctsInstallHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("mock cts, set caller PackageName :u9521.cts")
                param!!.result = "u9521.cts"
            }
        }

        XposedBridge.hookMethod(callerNameMe, ctsInstallHooker)
    }
}