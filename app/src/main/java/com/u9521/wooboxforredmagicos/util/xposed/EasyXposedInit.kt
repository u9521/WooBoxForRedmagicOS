package com.u9521.wooboxforredmagicos.util.xposed

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.Log
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
                EzXHelper.initHandleLoadPackage(lpparam)
                EzXHelper.setLogTag(app.logTag)
                EzXHelper.setToastTag(app.logTag)
                runCatching { app.handleLoadPackage(lpparam) }.onFailure {
                    Log.ix(
                        it,
                        "Failed call handleLoadPackage, package: ${app.packageName}"
                    )
                }
            }
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        EzXHelper.initZygote(startupParam!!)
    }

}