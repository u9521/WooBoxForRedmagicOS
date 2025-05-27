package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import android.app.SearchManager
import android.app.role.RoleManager
import android.content.Context
import android.os.Bundle
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object PowerWakeupAssist : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("long_power_key_wakeup_assist") {
        val phoneWMClazz = getDefaultCL().loadClass("com.android.server.policy.PhoneWindowManager")
        val powerLongPress =
            phoneWMClazz.getDeclaredMethod("powerLongPress", Long::class.javaPrimitiveType)
        val resolvePowerLongBH =
            phoneWMClazz.getDeclaredMethod("getResolvedLongPressOnPowerBehavior")
        val showGA = phoneWMClazz.getDeclaredMethod("showGlobalActions")
        val performHaptic = phoneWMClazz.getDeclaredMethod(
            "performHapticFeedback",
            Int::class.javaPrimitiveType,
            Boolean::class.javaPrimitiveType,
            String::class.java
        )
        val powerKeyHandled = phoneWMClazz.getDeclaredField("mPowerKeyHandled")
        val mContext = phoneWMClazz.getDeclaredField("mContext")
        val longPressHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                //power behavior 4 打开数字助理，1正常关机，2，3cts测试，5威图life，应该没人在威图手机上用这个模块吧
                var powerBH = resolvePowerLongBH.invoke(param!!.thisObject) as Int
                Log.i("powerKey long pressed. power behavior is: $powerBH")

                val context = mContext.get(param.thisObject) as Context
                val searchManager =
                    context.getSystemService(Context.SEARCH_SERVICE) as SearchManager
                val roleManager =
                    context.getSystemService(Context.ROLE_SERVICE) as RoleManager

                var defaultAssistPkgName: String? = null

                if (powerBH != 1) {
                    //获取默认数字助理
                    defaultAssistPkgName = roleManager.javaClass.getDeclaredMethod(
                        "getDefaultApplication",
                        String::class.java
                    ).invoke(roleManager, RoleManager.ROLE_ASSISTANT) as String?
                    if (defaultAssistPkgName == null) {
                        powerBH = 1
                        Log.wx(
                            "no default digital assist found,fallback to normal powerOff",
                            logInRelease = true
                        )
                    }
                }
                if (powerBH != 1) {
                    Log.i("default assist package name:$defaultAssistPkgName")
                    powerKeyHandled.setBoolean(param.thisObject, true)
                    performHaptic.invoke(
                        param.thisObject, 10003, false, "Power - Long Press - Go To Voice Assist"
                    )
                    //启动数字助理
                    searchManager.javaClass.getMethod(
                        "launchAssist", Bundle::class.java
                    ).invoke(searchManager, Bundle())
                    Log.i("finished handle Voice Assist")
                    param.result = null
                } else {
                    //处理正常关机
                    powerKeyHandled.setBoolean(param.thisObject, true)
                    performHaptic.invoke(
                        param.thisObject, 10003, false, "Power - Long Press - Global Actions"
                    )
                    showGA.invoke(param.thisObject)
                    Log.i("finish handle powerOff")
                    param.result = null
                }
            }
        }
        XposedBridge.hookMethod(powerLongPress, longPressHooker)
    }
}