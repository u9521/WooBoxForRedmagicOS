package com.u9521.wooboxforredmagicos.util.xposed

import com.u9521.wooboxforredmagicos.BuildConfig
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage


abstract class EasyXposedInit : IXposedHookLoadPackage, IXposedHookZygoteInit {

    private lateinit var packageParam: XC_LoadPackage.LoadPackageParam
    abstract val registeredApp: List<AppRegister>


    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        packageParam = lpparam!!
        registeredApp.forEach { app ->
            if ((lpparam.packageName in app.packageName) && (lpparam.processName in app.processName || app.processName.isEmpty())) {
                Log.logTag = app.logTag
                Log.isDebug = XSPUtils.getBoolean("debug_log", BuildConfig.DEBUG)
                Log.px(
                    "I", "start Hook: " + lpparam.packageName, logInRelease = true
                )
                runCatching { app.handleLoadPackage(lpparam) }.onFailure {
                    Log.ex(
                        "Failed call handleLoadPackage, package: ${app.packageName}", it, true
                    )
                }
                Log.px("I", "Finished hook " + lpparam.packageName, logInRelease = true)
            }
        }
    }
    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
    }
}