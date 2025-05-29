package com.u9521.wooboxforredmagicos.hook

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.hook.app.*
import com.u9521.wooboxforredmagicos.hook.app.android.DisableFlagSecure
import com.u9521.wooboxforredmagicos.util.xposed.EasyXposedInit
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry : EasyXposedInit() {
    private var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "WooboxConfig")

    override val registeredApp: List<AppRegister> = listOf(
        Android, //Android
        DoubleApp, //双开服务
        Launcher, //桌面
        MtpFileBrowser,//mtp文件浏览
        NfcService,//NFC服务
        PackageInstaller,//应用包安装程序
        PermissionController, //权限控制器
        Settings,//系统设置
        SystemUI, //系统界面
        SystemUpdater,//系统更新
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (prefs.getBoolean("main_switch", true)) {
            super.handleLoadPackage(lpparam)
        }
        val warnToastHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                Toast.makeText(
                    param!!.thisObject as Activity,
                    "Woobox注入了非推荐应用:(${lpparam!!.packageName})",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        if (!matchPackagename(registeredApp, lpparam!!.packageName)) {
            val oCMe = Activity::class.java.getDeclaredMethod("onCreate", Bundle::class.java)
            XposedBridge.hookMethod(oCMe, warnToastHooker)
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        super.initZygote(startupParam)
//        CorePatch().initZygote(startupParam)
        DisableFlagSecure().initZygote(startupParam)

    }

    private fun matchPackagename(arr: List<AppRegister>, target: String): Boolean {
        if (BuildConfig.APPLICATION_ID == target) {
            return true
        }
        for (subList in arr) {
            for (element in subList.packageName) {
                if (element == target) return true
            }
        }
        return false
    }
}