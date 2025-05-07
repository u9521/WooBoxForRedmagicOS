package com.u9521.wooboxforredmagicos.util.xposed.base

import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.DexKitBridge

abstract class HookRegister {
    private lateinit var lpparam: XC_LoadPackage.LoadPackageParam
    var isInit: Boolean = false
    var dexKitBridge: DexKitBridge? = null
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
}