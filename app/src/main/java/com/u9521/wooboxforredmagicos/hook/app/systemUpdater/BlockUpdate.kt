package com.u9521.wooboxforredmagicos.hook.app.systemUpdater

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object BlockUpdate : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val apMe =
            getDefaultCL().loadClass("android.os.UpdateEngine").getDeclaredMethod(
                "applyPayload",
                String::class.java,
                Long::class.java,
                Long::class.java,
                Array<String>::class.java
            )
        val applyPayloadHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val url = param!!.args[0] as String
                val offset = param.args[1] as Long
                val size = param.args[2] as Long
                val headerKeyValuePairs = param.args[3] as Array<*>
                if (XSPUtils.getBoolean("system_update_block_update", false)) {
                    param.result = null
                    Log.ex(
                        "已经拦截了一次系统更新：\n- URL: $url\n- Offset: $offset\n- Size: $size\n- headerKeyValuePairs: ${
                            headerKeyValuePairs.joinToString(", ")
                        }", logInRelease = true
                    )
                }
            }
        }
        XposedBridge.hookMethod(apMe, applyPayloadHooker)
    }
}