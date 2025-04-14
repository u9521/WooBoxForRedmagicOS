package com.u9521.wooboxforredmagicos.util.xposed

import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Member
import java.lang.reflect.Method


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

    companion object {
        @JvmStatic
        var m: Method? = null;

        @JvmStatic
        var deoptimizeMethod = m;

        init {
            try {
                //noinspection JavaReflectionMemberAccess
                m = XposedBridge::class.java.getDeclaredMethod(
                    "deoptimizeMethod",
                    Member::class.java
                )
            } catch (t: Throwable) {
                XposedBridge.log(t);
            }
            deoptimizeMethod = m;
        }
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun deoptimizeMethod(c: Class<*>, methodName: String) {
        for (m in c.declaredMethods) {
            if (deoptimizeMethod != null && m.name == methodName) {
                deoptimizeMethod!!.invoke(null, m)
                Log.i("Method Deoptimized $m")
            }
        }
    }

}