package com.u9521.wooboxforredmagicos.hook.app.permissioncontroller

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.u9521.wooboxforredmagicos.util.hasEnable
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
        val dACFClazzName = "com.android.permissioncontroller.role.ui.DefaultAppChildFragment"
        val meName = "onRoleChanged"
        val isCtsHooker = object : XC_MethodHook() {
            @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val caller = StackWalker.getInstance().walk { frames ->
                    frames.limit(10).filter { frame ->
                        frame.className == dACFClazzName && frame.methodName == meName
                    }.findFirst().orElse(null)
                }
                if (caller == null) {
                    return
                }
                param!!.result = true
            }
        }

        XposedBridge.hookMethod(ctsMe, isCtsHooker)
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