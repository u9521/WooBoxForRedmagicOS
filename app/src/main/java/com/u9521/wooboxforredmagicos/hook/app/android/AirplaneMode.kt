package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object AirplaneMode : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("airplane_mode_keep_bt") {
            val skipChange =
                getDefaultCL().loadClass("com.android.server.bluetooth.BluetoothAirplaneModeListener")
                    .getDeclaredMethod("shouldSkipAirplaneModeChange")
            val skipHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    Log.i("Airplane Skip BT change called")
                    param!!.result = true
                }
            }
            XposedBridge.hookMethod(skipChange, skipHooker)
        }
    }

    @SuppressLint("PrivateApi")
    fun wifiAirplaneHooker(cl: ClassLoader) {
        hasEnable("airplane_mode_keep_wlan") {
            val isAS = cl.loadClass("com.android.server.wifi.WifiSettingsStore")
                .getDeclaredMethod("isAirplaneSensitive")
            val aSHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = false
                    Log.i("Airplane Sensitive Called, skip change wifi and hotspot")
                }
            }
            XposedBridge.hookMethod(isAS, aSHooker)
            Log.i("hook isAirplaneSensitive success", logInRelease = true)
            // dont inline isAirplaneSensitive
            Deoptimizer.deoptimizeMethod(
                cl.loadClass("com.android.server.wifi.WifiSettingsStore"),
                "updateAirplaneModeTracker"
            )
        }
    }
}