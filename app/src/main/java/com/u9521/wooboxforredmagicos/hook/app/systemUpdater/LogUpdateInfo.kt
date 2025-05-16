package com.u9521.wooboxforredmagicos.hook.app.systemUpdater

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object LogUpdateInfo : HookRegister() {
    override fun init() {
        when (val me = findHttpexec(dexKitBridge!!)) {
            null -> Log.ex("find log update Failed")
            else -> me.createAfterHook {
                Log.ix("received a update:\n${it.result}")
            }.also { Log.ix("find and hook log update success") }
        }
    }

    fun findHttpexec(dexKitBridge: DexKitBridge): Method? {
        val methoddata = dexKitBridge.findClass {
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
        return methoddata?.getMethodInstance(getDefaultClassLoader())
    }
}