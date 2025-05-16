package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object UnhideBlackListApps : HookRegister() {
    override fun init() = hasEnable("launcher_Force_Show_Blacklist_apps") {
        findHidemethods(dexKitBridge!!).createBeforeHook {
            it.args[0] = true
        }
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
        return methodData.getMethodInstance(getDefaultClassLoader())
    }
}