package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import android.net.MacAddress
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object WLANServerHooker : HookRegister() {
    const val WIFI_SERVICE_CLASS = "com.android.server.wifi.WifiService"

    @SuppressLint("PrivateApi")
    override fun init() {
        var wifiServiceUnhook: XC_MethodHook.Unhook? = null
        val loadClassMe = getDefaultCL().loadClass("com.android.server.SystemServiceManager")
            .getDeclaredMethod("loadClassFromLoader", String::class.java, ClassLoader::class.java)
        val loadClassHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val clzName = param!!.args[0] as String
                val cl = param.args[1] as ClassLoader
                if (clzName == WIFI_SERVICE_CLASS) {
                    Log.i("found WifiService, start Hook", logInRelease = true)
                    runCatching {
                        // add your wifi service Hooker here
                        AirplaneMode.wifiAirplaneHooker(cl)
                        wlanTelecomCCHooker(cl)
                        wlanNativeHooker(cl)
                        wifiServiceUnhook!!.unhook()
                    }.onSuccess {
                        Log.i(
                            "hooked WifiService succeed,released loadClassFromLoader Hooker",
                            logInRelease = true
                        )
                    }.onFailure {
                        Log.ex("hook WifiService failed", it, logInRelease = true)
                    }
                }
            }
        }
        runCatching {
            wifiServiceUnhook = XposedBridge.hookMethod(loadClassMe, loadClassHooker)
        }.onSuccess {
            Log.i("Hook loadClassFromLoader Success", logInRelease = true)
        }.onFailure {
            Log.ex(
                "Hook loadClassFromLoader Failed, WifiService Hook wont work",
                it,
                logInRelease = true
            )
        }
    }

    private fun isValidCC(countryCode: String): Boolean {
        if (countryCode.length != 2) {
            return false
        }
        return countryCode.all { char ->
            char.isLetterOrDigit()
        }
    }

    private fun validMAC(macAddress: String): MacAddress? {
        try {
            val mac = MacAddress.fromString(macAddress)
            return mac
        } catch (e: IllegalArgumentException) {
            Log.ex("macAddress: $macAddress is invalid", e, logInRelease = true)
            return null
        }
    }

    @SuppressLint("PrivateApi")
    private fun wlanTelecomCCHooker(cl: ClassLoader) {
        hasEnable("custom_wlan_countrycode") {
            val setTelecomCC = cl.loadClass("com.android.server.wifi.WifiCountryCode")
                .getDeclaredMethod("setTelephonyCountryCode", String::class.java)
            val setTelecomCCHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val custCC = XSPUtils.getString("telecom_wlan_cc_val", "us")!!
                    if (isValidCC(custCC)) {
                        Log.ix("set country Code ${param!!.args[0] as String?} to $custCC")
                        param.args[0] = custCC
                        return
                    }
                    Log.ex("Invalid countryCode:$custCC,please check it", logInRelease = true)
                }
            }
            //this method not likely inline
            XposedBridge.hookMethod(setTelecomCC, setTelecomCCHooker)
            Log.i("hooked setTelephonyCountryCode", logInRelease = true)

//            MethodFinder.fromClass("com.android.server.wifi.WifiCountryCode", cl)
//                .filterByName("setTelephonyCountryCode").filterByParamTypes(
//                    String::class.java
//                ).first().createBeforeHook {
//                    val custCC = XSPUtils.getString("telecom_wlan_cc_val", "us")!!
//                    if (isValidCC(custCC)) {
//                        Log.ix("set country Code ${it.args[0] as String?} to $custCC")
//                        it.args[0] = custCC
//                        return@createBeforeHook
//                    }
//                    Log.ex("Invalid countryCode:$custCC,we dont hook that")
//                }
        }
    }

    //use at your own risk
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
        val setStaMacMe = wifiNativeClazz.getDeclaredMethod(
            "setStaMacAddress",
            String::class.java,
            MacAddress::class.java
        )

        val setStaMacMeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                hasEnable("pin_sta_mac_sw") {
                    Log.i("setStaMacAddress called:${param!!.args[0] as String},${param.args[1] as MacAddress}")
                    //才没有彩蛋呢
                    val macStr = XSPUtils.getString("pin_sta_mac", "66:31:32:35:39:75")!!
                    validMAC(macStr)?.let { mac ->
                        param.args[1] = mac
                        Log.i("override sta mac to:${mac}")
                    }
                }
            }
        }
        XposedBridge.hookMethod(setStaMacMe, setStaMacMeHooker)
        Log.i("hooked setStaMacAddress", logInRelease = true)

        // setApMacAddress AP MAC地址,改了有用的才能开热点
        val setApMacMe = wifiNativeClazz.getDeclaredMethod(
            "setApMacAddress",
            String::class.java,
            MacAddress::class.java
        )
        val setApMacMeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("setApMacAddress:${param!!.args[0] as String},${param.args[1] as MacAddress}")
                //才没有彩蛋呢
                val macStr = XSPUtils.getString("pin_ap_bssid", "b4:f3:cb:a7:b8:e7")!!
                validMAC(macStr)?.let { mac ->
                    param.args[1] = mac
                    Log.i("override AP mac to:${mac}")
                }
            }
        }
        XposedBridge.hookMethod(setApMacMe, setApMacMeHooker)
        Log.i("hooked setStaMacAddress", logInRelease = true)


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