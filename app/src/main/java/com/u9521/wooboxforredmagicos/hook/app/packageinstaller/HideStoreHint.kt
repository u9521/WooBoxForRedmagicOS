package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import android.annotation.SuppressLint
import android.view.View
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object HideStoreHint : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("installer_hide_store_hint") {
        val pIActivityClzz =
            getDefaultClassLoader().loadClass("com.android.packageinstaller.PackageInstallerActivity")
        val mMarketContainer = pIActivityClzz.getDeclaredField("mMarketContainer").apply {
            isAccessible = true
        }
        val mWarningUnknown = pIActivityClzz.getDeclaredField("mWarningUnknown").apply {
            isAccessible = true
        }
        val mDivider = pIActivityClzz.getDeclaredField("mDivider").apply {
            isAccessible = true
        }
        val bindUiMe = pIActivityClzz.getDeclaredMethod(
            "bindUi",
            Int::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType
        )
        val bindUiPermMe = pIActivityClzz.getDeclaredMethod(
            "bindUiPerm",
            Int::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType
        )
        val bindUIHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                Log.i("Trying to remove Store recommend")
                (mMarketContainer.get(param!!.thisObject) as View).visibility = View.GONE
                (mWarningUnknown.get(param.thisObject) as View).visibility = View.GONE
                (mDivider.get(param.thisObject) as View).visibility = View.GONE
            }
        }
        XposedBridge.hookMethod(bindUiMe, bindUIHooker)
        XposedBridge.hookMethod(bindUiPermMe, bindUIHooker)
    }
}