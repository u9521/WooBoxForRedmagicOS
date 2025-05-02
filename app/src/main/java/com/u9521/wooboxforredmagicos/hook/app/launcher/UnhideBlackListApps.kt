package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType

object UnhideBlackListApps : HookRegister() {
    override fun init() = hasEnable("launcher_Force_Show_Blacklist_apps") {
        lateinit var configResourceClazzName: String
        lateinit var configResourceMethodName: String
        DexKitBridge.create(getLoadPackageParam().appInfo.sourceDir)
            .use { dexKitBridge: DexKitBridge ->
                findHidemethods(dexKitBridge).also { (a, b) ->
                    configResourceClazzName = a
                    configResourceMethodName = b
                }
            }
        MethodFinder.fromClass(configResourceClazzName)
            .filterByName(configResourceMethodName)
            .first().createBeforeHook {
                it.args[0] = true
            }
    }

    private fun findHidemethods(bridge: DexKitBridge): Pair<String, String> {
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
        return Pair(classData.name, methodData.name)
    }
}