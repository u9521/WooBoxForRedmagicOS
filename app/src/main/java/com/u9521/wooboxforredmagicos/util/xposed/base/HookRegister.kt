package com.u9521.wooboxforredmagicos.util.xposed.base

import com.github.kyuubiran.ezxhelper.Log
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Member
import java.lang.reflect.Method

abstract class HookRegister {
    private lateinit var lpparam: XC_LoadPackage.LoadPackageParam
    var isInit: Boolean = false
    abstract fun init()

    fun setLoadPackageParam(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        lpparam = loadPackageParam
    }

    @Suppress("MemberVisibilityCanBePrivate")
    protected fun getLoadPackageParam(): XC_LoadPackage.LoadPackageParam {
        if (!this::lpparam.isInitialized) {
            throw RuntimeException("lpparam should be initialized")
        }
        return lpparam
    }

    @Suppress("unused")
    protected fun getDefaultClassLoader(): ClassLoader {
        return getLoadPackageParam().classLoader
    }

    companion object {
        @JvmStatic
        var m: Method? = null

        @JvmStatic
        var deoptimizeMethod = m

        init {
            try {
                //noinspection JavaReflectionMemberAccess
                m = XposedBridge::class.java.getDeclaredMethod(
                    "deoptimizeMethod",
                    Member::class.java
                )
            } catch (t: Throwable) {
                XposedBridge.log(t)
            }
            deoptimizeMethod = m
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