package com.u9521.wooboxforredmagicos.hook.app.doubleApp

import android.annotation.SuppressLint
import android.content.Context
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object DoubleAnyApp : HookRegister() {

    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("double_any_app") {
        val updateUtilsClazz = getDefaultCL().loadClass("com.zte.cn.doubleapp.common.UpdateUtils")
        val supportApp = updateUtilsClazz.getDeclaredMethod("getSupportApps")
        val mContext = updateUtilsClazz.getDeclaredField("mContext").apply { isAccessible = true }
        val supportAppHooker = object : XC_MethodHook() {
            @SuppressLint("QueryPermissionsNeeded")
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val context = mContext.get(param!!.thisObject) as Context
                val packages = context.packageManager.getInstalledPackages(0)
                param.result = packages.map { it.packageName }
            }
        }
        XposedBridge.hookMethod(supportApp, supportAppHooker)
    }
}