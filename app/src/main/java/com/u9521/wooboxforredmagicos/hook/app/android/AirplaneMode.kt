package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object AirplaneMode : HookRegister() {
    override fun init() {
        hasEnable("airplane_mode_keep_bt") {
            MethodFinder.fromClass("com.android.server.bluetooth.BluetoothAirplaneModeListener")
                .filterByName("shouldSkipAirplaneModeChange").first().createBeforeHook {
                    it.result = true
                }
        }
        hasEnable("airplane_mode_keep_wlan") {
            try {
                var wifiServiceHooker: XC_MethodHook.Unhook? = null
                wifiServiceHooker =
                    MethodFinder.fromClass("com.android.server.SystemServiceManager")
                        .filterByName("loadClassFromLoader").filterByParamTypes(
                            String::class.java, ClassLoader::class.java
                        ).first().createBeforeHook {
                            val clzName = it.args[0] as String
                            val cl = it.args[1] as ClassLoader
                            if (clzName == "com.android.server.wifi.WifiService") {
                                wifiServerHooker(cl)
                                wifiServiceHooker!!.unhook()
                            }
                        }
            } catch (t: Throwable) {
                Log.ex(t, "hook WifiService failed")
            }
        }
    }

    @SuppressLint("PrivateApi")
    private fun wifiServerHooker(cl: ClassLoader) {
        MethodFinder.fromClass(
            "com.android.server.wifi.WifiSettingsStore", cl
        ).filterByName("isAirplaneSensitive").first()
            .createAfterHook {
                it.result = false
            }
        Log.i("hook isAirplaneSensitive success")
//        MethodFinder.fromClass(
//            "com.android.server.wifi.WifiSettingsStoreDual", cl
//        ).filterByName("handleAirplaneModeToggled").first()
//            .createBeforeHook {
//                Log.i("Dual handleAirplaneModeToggled called")
//                it.result = null
//            }
//        Log.i("Dual hook handleAirplaneModeToggled success")
        // dont inline isAirplaneSensitive
        Deoptimizer.deoptimizeMethod(
            cl.loadClass("com.android.server.wifi.WifiSettingsStore"),
            "updateAirplaneModeTracker"
        )
    }
}