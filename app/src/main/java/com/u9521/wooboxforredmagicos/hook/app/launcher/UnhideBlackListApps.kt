package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object UnhideBlackListApps : HookRegister() {
    override fun init() = hasEnable("launcher_force_show_blacklist_apps") {
        val unHideAppMe = findHidemethods(dexKitBridge!!)
        val unHideAppHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.args[0] = true
                Log.i("start unhide blacklist app", logInRelease = true)
            }
        }
        XposedBridge.hookMethod(unHideAppMe, unHideAppHooker)
    }

    private fun findHidemethods(bridge: DexKitBridge): Method {
        //ConfigResource
        val classData = bridge.findClass {
            searchPackages("com.android.launcher3.resource")
            matcher {
                methods {
                    add {
                        name = "<init>"
                        usingStrings(
                            listOf("com.tencent.tim", "com.tencent.qqpimsecure"),
                            StringMatchType.Equals
                        )
                    }
                }
            }
        }.singleOrNull() ?: error("ConfigResourceClass not find")
        val methodData = classData.findMethod {
            matcher {
                usingStrings(listOf("config_mylauncher", "config"), StringMatchType.Equals)
                paramTypes(Boolean::class.javaPrimitiveType)
            }
        }.singleOrNull() ?: error("ConfigResource->method not find")
        return methodData.getMethodInstance(getDefaultCL())
    }
}