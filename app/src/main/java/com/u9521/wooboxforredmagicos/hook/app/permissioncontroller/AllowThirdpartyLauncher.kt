package com.u9521.wooboxforredmagicos.hook.app.permissioncontroller

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object AllowThirdpartyLauncher : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("allow_thirdparty_launcher") {

        val ctsMe = findisctsmethod(dexKitBridge!!)
        val isCtsHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = true
            }
        }
        val roleChangedHooker = object : XC_MethodHook() {
            var hooker: Unhook? = null
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("start to mock CTS")
                hooker = XposedBridge.hookMethod(ctsMe, isCtsHooker)
            }

            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                hooker!!.unhook()
                Log.i("mock CTS finished, hooker released")
            }
        }
        val roleChangeMe =
            getDefaultCL().loadClass("com.android.permissioncontroller.role.ui.DefaultAppChildFragment")
                .getDeclaredMethod("onRoleChanged", List::class.java)

        XposedBridge.hookMethod(roleChangeMe, roleChangedHooker)

    }

    private fun findisctsmethod(bridge: DexKitBridge): Method {
        //ConfigResource
        val classData = bridge.findClass {
            searchPackages("com.android.permissioncontroller.permission.utils")
            matcher {
                methods {
                    add {
                        name = "<clinit>"
                        usingStrings(
                            listOf("com.android.calendar", "com.zte.manual"), StringMatchType.Equals
                        )
                    }
                }
            }
        }.singleOrNull() ?: error("utilsClass not find")
        val methodData = classData.findMethod {
            matcher {
                paramTypes(String::class.java)
                returnType(Boolean::class.javaPrimitiveType!!)
                usingStrings(listOf("android.", ".cts."), StringMatchType.Equals)
            }
        }.singleOrNull() ?: error("utils->isctsMethod not find")
        return methodData.getMethodInstance(getDefaultCL())
    }
}