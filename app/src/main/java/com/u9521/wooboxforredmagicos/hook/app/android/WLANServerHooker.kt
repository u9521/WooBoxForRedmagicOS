package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import android.net.MacAddress
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object WLANServerHooker : HookRegister() {
    override fun init() {
        try {
            var wifiServiceHooker: XC_MethodHook.Unhook? = null
            wifiServiceHooker = MethodFinder.fromClass("com.android.server.SystemServiceManager")
                .filterByName("loadClassFromLoader").filterByParamTypes(
                    String::class.java, ClassLoader::class.java
                ).first().createBeforeHook {
                    val clzName = it.args[0] as String
                    val cl = it.args[1] as ClassLoader
                    if (clzName == "com.android.server.wifi.WifiService") {
                        AirplaneMode.wifiAirplaneHooker(cl)
                        wlanTelecomCCHooker(cl)
                        wlanNativeHooker(cl)
                        wifiServiceHooker!!.unhook()
                        Log.ix("hooked WifiService")
                    }
                }
        } catch (t: Throwable) {
            Log.ex(t, "hook WifiService failed")
        }
    }

    private fun isValidCC(countrycode: String): Boolean {
        if (countrycode.length != 2) {
            return false
        }
        return countrycode.all { char ->
            char.isLetterOrDigit()
        }
    }

    private fun validMAC(macAddress: String): MacAddress? {
        try {
            val mac = MacAddress.fromString(macAddress)
            return mac
        } catch (e: IllegalArgumentException) {
            Log.ex("macAddress: $macAddress is invalid", e)
            return null
        }
    }

    @SuppressLint("PrivateApi")
    private fun wlanTelecomCCHooker(cl: ClassLoader) {
        hasEnable("custom_wlan_countrycode") {

            MethodFinder.fromClass("com.android.server.wifi.WifiCountryCode", cl)
                .filterByName("setTelephonyCountryCode").filterByParamTypes(
                    String::class.java
                ).first().createBeforeHook {
                    val custCC = XSPUtils.getString("telecom_wlan_cc_val", "us")!!
                    if (isValidCC(custCC)) {
                        Log.ix("set country Code ${it.args[0] as String?} to $custCC")
                        it.args[0] = custCC
                        return@createBeforeHook
                    }
                    Log.ex("Invalid countryCode:$custCC,we dont hook that")
                }
            Log.ix("hooked setTelephonyCountryCode success")
            //this method not likely inline
//            Deoptimizer.deoptimizeMethod(
//                cl.loadClass("com.android.server.wifi.WifiCountryCode"),
//                "updateAirplaneModeTracker"
//            )
        }
    }

    // not test yet ,use at your own risk
    @SuppressLint("PrivateApi")
    private fun wlanNativeHooker(cl: ClassLoader) {
        val wifiNativeClazz = cl.loadClass("com.android.server.wifi.WifiNative")
        // setStaCountryCode 终端国家码,需要进一步分析
//        MethodFinder.fromClass(wifiNativeClazz).filterByName("setStaCountryCode")
//            .filterByParamTypes(String::class.java, String::class.java).first().createBeforeHook {
////                Log.i("setStaCountryCode called:${it.args[0] as String},${it.args[1] as String}")
//            }
        // setApCountryCode 热点国家码 ,需要进一步分析
//        MethodFinder.fromClass(wifiNativeClazz).filterByName("setApCountryCode")
//            .filterByParamTypes(String::class.java, String::class.java).first().createBeforeHook {
////                Log.i("setApCountryCode called:${it.args[0] as String},${it.args[1] as String}")
//            }
        // setStaMacAddress 终端MAC地址
        MethodFinder.fromClass(wifiNativeClazz).filterByName("setStaMacAddress")
            .filterByParamTypes(String::class.java, MacAddress::class.java).first()
            .createBeforeHook {
                hasEnable("pin_sta_mac_sw") {
                    Log.i("setStaMacAddress called:${it.args[0] as String},${it.args[1] as MacAddress}")
                    //才没有彩蛋呢
                    val macStr = XSPUtils.getString("pin_sta_mac", "66:31:32:35:39:75")!!
                    validMAC(macStr)?.let { mac ->
                        it.args[1] = mac
                        Log.i("mod mac to:${mac}")
                    }
                }
            }
        // setApMacAddress AP MAC地址,改了有用的才能开热点
        MethodFinder.fromClass(wifiNativeClazz).filterByName("setApMacAddress")
            .filterByParamTypes(String::class.java, MacAddress::class.java).first()
            .createBeforeHook {
                hasEnable("pin_ap_bssid_sw") {
                    Log.i("setApMacAddress called:${it.args[0] as String},${it.args[1] as MacAddress}")
                    //才没有彩蛋呢
                    val macStr = XSPUtils.getString("pin_ap_bssid", "b4:f3:cb:a7:b8:e7")!!
                    validMAC(macStr)?.let { mac ->
                        it.args[1] = mac
                        Log.i("mod bssid to:${mac}")
                    }
                }
            }

        Deoptimizer.deoptimizeMethods(
            cl.loadClass("com.android.server.wifi.SoftApManager"), "setMacAddress", "setCountryCode"
        )
        Deoptimizer.deoptimizeMethods(
            cl.loadClass("com.android.server.wifi.ClientModeImpl"),
            "configureRandomizedMacAddress",
            "setCurrentMacToFactoryMac",
            "onUpChanged",
            "setCountryCode"
        )
    }

}