package com.u9521.wooboxforredmagicos.hook.app.permissioncontroller

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object AllowThirdpartyLauncher : HookRegister() {
    override fun init() = hasEnable("allow_thirdparty_launcher") {

        val ctsMe = findisctsmethod(dexKitBridge!!)

        MethodFinder.fromClass("com.android.permissioncontroller.role.ui.DefaultAppChildFragment")
            .filterByName("onRoleChanged").first().createHook {
                var hooker: XC_MethodHook.Unhook? = null
                before {
                    hooker = ctsMe.createBeforeHook {
                        it.result = true
                    }
                }
                after {
                    hooker!!.unhook()
                }
            }
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
        return methodData.getMethodInstance(getDefaultClassLoader())
    }
}