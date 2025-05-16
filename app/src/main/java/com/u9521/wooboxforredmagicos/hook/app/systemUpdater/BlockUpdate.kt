package com.u9521.wooboxforredmagicos.hook.app.systemUpdater

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import org.luckypray.dexkit.DexKitBridge
import org.luckypray.dexkit.query.enums.StringMatchType
import java.lang.reflect.Method

object BlockUpdate : HookRegister() {
    override fun init() {
        MethodFinder.fromClass("android.os.UpdateEngine").filterByName("applyPayload")
            .filterByParamTypes(
                String::class.java, Long::class.java, Long::class.java, Array<String>::class.java
            ).first().createBeforeHook {
                // String url, long offset, long size, String[] headerKeyValuePairs
                val url = it.args[0] as String
                val offset = it.args[1] as Long
                val size = it.args[2] as Long
                val headerKeyValuePairs = it.args[3] as Array<String>
                if (XSPUtils.getBoolean("system_update_block_update", false)) {
                    Log.wx("Last Line of Defense :Block applyPayload,please check dexKit status")
                    it.result = null
                    Log.ex(
                        "已经拦截了一次系统更新：\n- URL: $url\n- Offset: $offset\n- Size: $size\n- headerKeyValuePairs: ${
                            headerKeyValuePairs.joinToString(", ")
                        }"
                    )
                }
            }
        when (val me = findApplyPayload(dexKitBridge!!)) {
            null -> Log.ex("findApplyPayload Failed")
            else -> me.createBeforeHook {
                val url = it.args[0] as String
                val offset = it.args[1] as Long
                val size = it.args[2] as Long
                val headerKeyValuePairs = it.args[3] as Array<String>
                if (XSPUtils.getBoolean("system_update_block_update", false)) {
                    it.result = null
                    Log.ex(
                        "已经拦截了一次系统更新：\n- URL: $url\n- Offset: $offset\n- Size: $size\n- headerKeyValuePairs: ${
                            headerKeyValuePairs.joinToString(", ")
                        }"
                    )
                }
            }.also { Log.ix("find and hook ApplyPayload success") }
        }
    }

    fun findApplyPayload(dexKitBridge: DexKitBridge): Method? {
        val methoddata = dexKitBridge.findClass {
            searchPackages("com.zte.abupdate.util")
            matcher {
                methods {
                    add {
                        usingStrings(
                            listOf("Exception in applyPayload", "zdmlog"), StringMatchType.Equals
                        )
                    }
                }
            }
        }.findMethod {
            matcher {
                paramTypes(
                    String::class.java,
                    Long::class.java,
                    Long::class.java,
                    Array<String>::class.java
                )
            }
        }.singleOrNull()
        return methoddata?.getMethodInstance(getDefaultClassLoader())
    }
}