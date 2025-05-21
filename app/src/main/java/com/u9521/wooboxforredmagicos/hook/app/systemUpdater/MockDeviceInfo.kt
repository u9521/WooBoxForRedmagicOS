package com.u9521.wooboxforredmagicos.hook.app.systemUpdater

import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method
import java.nio.ByteBuffer

object MockDeviceInfo : HookRegister() {
    override fun init() {
        // model
        val modelHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_model_sw", false)) {
                    param!!.result = XSPUtils.getString("system_update_mock_model", "NX769J")
                }
            }
        }
        when (val me = findDeviceModel(dexKitBridge!!)) {
            null -> Log.ex("find DeviceModel Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, modelHooker)
                .also { Log.ix("find and hook DeviceModel success at ${me.declaringClass.name} -> ${me.name}") }
        }
        // imei
        val imeiHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_imei_sw", false)) {
                    param!!.result =
                        XSPUtils.getString("system_update_mock_imei", "114514191981000")
                }
            }
        }
        val l = findImei(dexKitBridge!!)
        if (l.isNotEmpty()) {
            l.forEach {
                XposedBridge.hookMethod(it, imeiHooker)
                Log.ix("find and hook DeviceImei success at ${it.declaringClass.name} -> ${it.name}")
            }
        } else {
            Log.ex("find DeviceImei Failed", logInRelease = true)
        }
        //locale
        val localeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_local_sw", false)) {
                    val localStr = XSPUtils.getString("system_update_mock_local", "zh_CN")
                    val len = localStr!!.length
                    val barr = param!!.args[1] as ByteArray?
                    if (barr != null) {
                        ByteBuffer.wrap(barr).put(localStr.toByteArray())
                    }
                    param.result = len
                }
            }
        }
        when (val me = findLocale(dexKitBridge!!)) {
            null -> Log.ex("find Locale Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, localeHooker)
                .also { Log.ix("find and hook Locale success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //appSignatureMD5+appVersioncode
        val appVerHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_sign_sw", false)) {
                    param!!.result = XSPUtils.getString(
                        "system_update_mock_sign",
                        "a2e44d1795185b8e3281444007effb7f_UNJ.REDMAGIC.FOTA.14.0.0.2409191549"
                    )
                }
            }
        }
        when (val me = findSignandVername(dexKitBridge!!)) {
            null -> Log.ex("find sign Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, appVerHooker)
                .also { Log.ix("find and hook sign success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //build fingerprint
        val bfHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_fingerprint_sw", false)) {
                    val bFPStr = XSPUtils.getString(
                        "system_update_mock_fingerprint",
                        "nubia/NX769J/NX769J:14/UKQ1.230917.001/20250310.231626:user/release-keys"
                    )
                    val len = bFPStr!!.length
                    val barr = param!!.args[1] as ByteArray?
                    if (barr != null) {
                        ByteBuffer.wrap(barr).put(bFPStr.toByteArray())
                    }
                    param.result = len
                }
            }
        }
        when (val me = findBuildFP(dexKitBridge!!)) {
            null -> Log.ex("find build fingerprint Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, bfHooker)
                .also { Log.ix("find and hook build fingerprint success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //system version
        //system Build.DISPLAY
        val buildDisplayHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_build_display_sw", false)) {
                    param!!.result = XSPUtils.getString(
                        "system_update_mock_build_display", "RedMagicOS9.5.25_NX769J_NY"
                    )
                }
            }
        }
        when (val me = findSystemBuildVer(dexKitBridge!!)) {
            null -> Log.ex("find Build.DISPLAY Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, buildDisplayHooker)
                .also { Log.ix("find and Build.DISPLAY success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //inner version
        val innerVerHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_system_inner_version_sw", false)) {
                    param!!.result = XSPUtils.getString(
                        "system_update_mock_system_inner_version", "GEN_CN_NX769SV1.5.0B25"
                    )
                }
            }
        }
        when (val me = findInnerVer(dexKitBridge!!)) {
            null -> Log.ex("find inner version Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, innerVerHooker)
                .also { Log.ix("find and inner version success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //VariantId
        val variantIdHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_variant_id_sw", false)) {
                    param!!.result = XSPUtils.getString(
                        "system_update_mock_variant_id", "GEN_NY_CN"
                    )
                }
            }
        }
        when (val me = findVariantId(dexKitBridge!!)) {
            null -> Log.ex("find VariantId Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, variantIdHooker)
                .also { Log.ix("find and VariantId success at ${me.declaringClass.name} -> ${me.name}") }
        }
        //mock manufacture
        val manuHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (XSPUtils.getBoolean("system_update_mock_manufacture_sw", false)) {
                    val manufactureStr = XSPUtils.getString(
                        "system_update_mock_manufacture", "ZTE"
                    )
                    val len = manufactureStr!!.length
                    val barr = param!!.args[1] as ByteArray?
                    if (barr != null) {
                        ByteBuffer.wrap(barr).put(manufactureStr.toByteArray())
                    }
                    param.result = len
                }
            }
        }

        when (val me = findManu(dexKitBridge!!)) {
            null -> Log.ex("find manufacture Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, manuHooker)
                .also { Log.ix("find and hook manufacture success at ${me.declaringClass.name} -> ${me.name}") }
        }

    }

    fun findDeviceModel(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            matcher {
                className = "com.zte.zdm.mo.DevInfo"
            }
        }.findMethod {
            matcher {
                addUsingField {
                    name = "MODEL"
                }
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findImei(dexKitBridge: DexKitBridge): List<Method> {
        val m = dexKitBridge.findClass {
            searchPackages("com.zte.zdm.application.util")
            matcher {
                usingStrings(listOf("cannotgetimei", "getDeviceId"), StringMatchType.Equals)
            }
        }.findMethod {
            matcher {
                returnType = "java.lang.String"
            }
        }
        val methodsList = mutableListOf<Method>()
        for (mData in m) {
            methodsList.add(mData.getMethodInstance(getDefaultCL()))
        }
        return methodsList
    }

    fun findLocale(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            matcher {
                className = "com.zte.zdm.mo.DevInfo"
            }
        }.findMethod {
            matcher {
                invokeMethods {
                    add {
                        descriptor = "Ljava/util/Locale;->getDefault()Ljava/util/Locale;"
                    }
                }
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findSignandVername(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            matcher {
                className = "com.zte.zdm.mo.DevInfoEX"
            }
        }.findMethod {
            matcher {
                invokeMethods {
                    add {
                        descriptor =
                            "Landroid/content/pm/PackageManager;->getPackageInfo(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;"
                    }
                }
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findBuildFP(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            matcher {
                className = "com.zte.zdm.mo.DevInfoEX"
            }
        }.findMethod {
            matcher {
                addUsingString("ro.build.fingerprint", StringMatchType.Equals)
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findSystemBuildVer(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            searchPackages("com.zte.zdm.application.util")
            matcher {
                usingStrings(
                    listOf("ro.build.inner.version", "ro.build.sw_internal_version"),
                    StringMatchType.Equals
                )
            }
        }.findMethod {
            matcher {
                addUsingString("apps.setting.product.release", StringMatchType.Equals)
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findInnerVer(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            searchPackages("com.zte.zdm.application.util")
            matcher {
                usingStrings(
                    listOf("ro.build.inner.version", "ro.build.sw_internal_version"),
                    StringMatchType.Equals
                )
            }
        }.findMethod {
            matcher {
                usingStrings(
                    listOf("ro.build.inner.version", "ro.build.sw_internal_version"),
                    StringMatchType.Equals
                )
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findVariantId(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            searchPackages("com.zte.zdm.application.util")
            matcher {
                usingStrings(
                    listOf("ZTE_FEATURE_TRANSFORM_LEGACY", "getCurrentCustVariantId"),
                    StringMatchType.Equals
                )
            }
        }.findMethod {
            matcher {
                usingStrings(
                    listOf("com.zte.feature.Feature", "ZTE_FEATURE_TRANSFORM"),
                    StringMatchType.Equals
                )
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }

    fun findManu(dexKitBridge: DexKitBridge): Method? {
        val m = dexKitBridge.findClass {
            matcher {
                className = "com.zte.zdm.mo.DevInfo"
            }
        }.findMethod {
            matcher {
                usingStrings(listOf("NUBIA", "ZTE"), StringMatchType.Equals)
            }
        }.singleOrNull()
        return m?.getMethodInstance(getDefaultCL())
    }
}