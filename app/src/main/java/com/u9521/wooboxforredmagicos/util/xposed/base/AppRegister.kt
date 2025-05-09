package com.u9521.wooboxforredmagicos.util.xposed.base

import com.github.kyuubiran.ezxhelper.Log
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_LoadPackage
import org.luckypray.dexkit.DexKitBridge

@Suppress("unused", "RedundantSuppression")
abstract class AppRegister : IXposedHookLoadPackage {
    abstract val packageName: List<String>
    abstract val processName: List<String>
    abstract val logTag: String
    abstract val loadDexkit: Boolean
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {}
    private var dexKitBridge: DexKitBridge? = null
    protected fun autoInitHooks(
        lpparam: XC_LoadPackage.LoadPackageParam,
        vararg hook: HookRegister
    ) {
        if (loadDexkit) {
            System.loadLibrary("dexkit")
            dexKitBridge = DexKitBridge.create(lpparam.appInfo.sourceDir)
        }
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.setLoadPackageParam(lpparam)
                it.dexKitBridge = dexKitBridge
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.fold(
                onSuccess = {},
                onFailure = { Log.ix(it, "Failed init hook: ${it.javaClass.simpleName}") })
        }
        if (loadDexkit) {
            dexKitBridge!!.close()
        }
    }
}