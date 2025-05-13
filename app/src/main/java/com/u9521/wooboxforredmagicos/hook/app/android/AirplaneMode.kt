package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object AirplaneMode : HookRegister() {
    override fun init() {
        hasEnable("airplane_mode_keep_bt") {
            MethodFinder.fromClass("com.android.server.bluetooth.BluetoothAirplaneModeListener")
                .filterByName("shouldSkipAirplaneModeChange").first().createBeforeHook {
                    it.result = true
                }
        }
    }

    @SuppressLint("PrivateApi")
    fun wifiAirplaneHooker(cl: ClassLoader) {
        hasEnable("airplane_mode_keep_wlan") {
            MethodFinder.fromClass(
                "com.android.server.wifi.WifiSettingsStore", cl
            ).filterByName("isAirplaneSensitive").first()
                .createAfterHook {
                    it.result = false
                }
            Log.i("hook isAirplaneSensitive success")
            // dont inline isAirplaneSensitive
            Deoptimizer.deoptimizeMethod(
                cl.loadClass("com.android.server.wifi.WifiSettingsStore"),
                "updateAirplaneModeTracker"
            )
        }
    }
}