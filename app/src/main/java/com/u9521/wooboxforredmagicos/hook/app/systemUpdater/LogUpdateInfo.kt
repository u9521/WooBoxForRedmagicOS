package com.u9521.wooboxforredmagicos.hook.app.systemUpdater

import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object LogUpdateInfo : HookRegister() {
    override fun init() {
        val logUpdateHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                Log.ix("received a update:\n${param!!.result}", logInRelease = true)
            }
        }

        when (val me = findHttpExec(dexKitBridge!!)) {
            null -> Log.ex("find log update Failed", logInRelease = true)
            else -> XposedBridge.hookMethod(me, logUpdateHooker)
                .also { Log.ix("find and hook log update success at ${me.declaringClass.name} -> ${me.name}") }
        }
    }

    fun findHttpExec(dexKitBridge: DexKitBridge): Method? {
        val methodData = dexKitBridge.findClass {
            searchPackages("com.zte.zdm.framework.http")
            matcher {
                addUsingString("Content-Length", StringMatchType.Equals)
                addUsingString("Content-Type", StringMatchType.Equals)
            }
        }.findMethod {
            matcher {
                paramTypes(
                    String::class.java,
                    String::class.java,
                )
                invokeMethods {
                    add {
                        descriptor = "Ljava/lang/String;->getBytes(Ljava/lang/String;)[B"
                    }
                }
            }
        }.singleOrNull()
        return methodData?.getMethodInstance(getDefaultCL())
    }
}