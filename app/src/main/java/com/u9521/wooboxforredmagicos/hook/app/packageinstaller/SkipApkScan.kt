package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object SkipApkScan : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("skip_apk_scan") {
        //skip scan
        // ZTE_FEATURE_ODM_VERTU
        val sIClazz =
            getDefaultClassLoader().loadClass("android.content.pm.PackageInstaller\$SessionInfo")
        val scanMe =
            getDefaultClassLoader().loadClass("com.android.packageinstaller.InstallStaging\$StagingAsyncTask")
                .getDeclaredMethod("onPostExecute", sIClazz)
        val scanHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("trying to pass apk scan")
                val vertuClazz =
                    getDefaultClassLoader().loadClass("com.android.packageinstaller.PackageUtil")
                vertuClazz.getDeclaredField("ZTE_FEATURE_ODM_VERTU").apply { isAccessible = true }
                    .setBoolean(null, true)
            }
        }
        XposedBridge.hookMethod(scanMe, scanHooker)
    }
}