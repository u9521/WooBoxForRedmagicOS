package com.u9521.wooboxforredmagicos.hook.app.permissioncontroller

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType

object AllowThirdpartyLauncher : HookRegister() {
    override fun init() = hasEnable("allow_thirdparty_launcher") {
        lateinit var utilsClazzName: String
        lateinit var isctsPackageMethodName: String
        DexKitBridge.create(getLoadPackageParam().appInfo.sourceDir)
            .use { dexKitBridge: DexKitBridge ->
                findisctsmethod(dexKitBridge).also { (a, b) ->
                    utilsClazzName = a
                    isctsPackageMethodName = b
                }
            }
        MethodFinder.fromClass("com.android.permissioncontroller.role.ui.DefaultAppChildFragment")
            .filterByName("onRoleChanged").first().createHook {
                var hooker: XC_MethodHook.Unhook? = null
                before {
                    hooker = MethodFinder.fromClass(utilsClazzName)
                        .filterByName(isctsPackageMethodName)
                        .first().createBeforeHook {
                            it.result = true
                        }
                }
                after {
                    hooker!!.unhook()
                }
            }
    }

    private fun findisctsmethod(bridge: DexKitBridge): Pair<String, String> {
        //ConfigResource
        val classData = bridge.findClass {
            searchPackages("com.android.permissioncontroller.permission.utils")
            matcher {
                methods {
                    add {
                        name = "<clinit>"
                        usingStrings(
                            listOf("com.android.calendar", "com.zte.manual"),
                            StringMatchType.Equals
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
        return Pair(classData.name, methodData.name)
    }
}