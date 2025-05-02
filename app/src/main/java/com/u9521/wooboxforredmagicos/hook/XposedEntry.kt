package com.u9521.wooboxforredmagicos.hook

import android.app.Activity
import android.widget.Toast
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.hook.app.Android
import com.u9521.wooboxforredmagicos.hook.app.Launcher
import com.u9521.wooboxforredmagicos.hook.app.PackageInstaller
import com.u9521.wooboxforredmagicos.hook.app.PermissionController
import com.u9521.wooboxforredmagicos.hook.app.Settings
import com.u9521.wooboxforredmagicos.hook.app.SystemUI
import com.u9521.wooboxforredmagicos.hook.app.android.DisableFlagSecure
import com.u9521.wooboxforredmagicos.hook.app.android.LogBlocker
import com.u9521.wooboxforredmagicos.util.xposed.EasyXposedInit
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XSharedPreferences
import de.robv.android.xposed.callbacks.XC_LoadPackage

class XposedEntry : EasyXposedInit() {
    private var prefs = XSharedPreferences(BuildConfig.APPLICATION_ID, "WooboxConfig")

    override val registeredApp: List<AppRegister> = listOf(
        Android, //Android
        SystemUI, //系统界面
        Launcher, //桌面
//        AlarmClock, //时钟
//        SecurityCenter, //安全中心
        PermissionController, //权限控制器
        PackageInstaller,//应用包安装程序
        Settings,//系统设置
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        if (prefs.getBoolean("main_switch", true)) {
            super.handleLoadPackage(lpparam)
        }
        Log.i("hooked " + lpparam!!.packageName)
        if (!matchPackagename(registeredApp, lpparam.packageName)) {
            MethodFinder.fromClass(Activity::class.java).filterByName("onCreate").first()
                .createBeforeHook {
                    Toast.makeText(
                        it.thisObject as Activity,
                        "注意:Woobox注入了非推荐应用",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        super.initZygote(startupParam)
//        CorePatch().initZygote(startupParam)
        DisableFlagSecure().initZygote(startupParam)
        LogBlocker().initZygote(startupParam!!)

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