package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object SyetemServerLogBlocker : HookRegister() {
    private val deoptMap: HashMap<String, Array<String>> = HashMap()
    private val noServerLog = XSPUtils.getBoolean("block_system_server_log", false)
    private val nullHooker = object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            param!!.result = null
        }
    }

    @SuppressLint("PrivateApi")
    override fun init() {
        val telecomLogClazz = getDefaultCL().loadClass("android.telecom.Log")
        val telecomLogBlMethods = arrayOf("d", "e", "i", "v", "w", "wtf")
//        val logClazz = ClassUtils.loadClass("android.util.Log")
        deoptMap["android.telecom.Logging.SessionManager"] =
            arrayOf("endParentSessions", "startSession", "cleanupStaleSessions")
        if (noServerLog) {
            doDeoptimize()
            telecomLogClazz.declaredMethods.onEach {
                if (it.name in telecomLogBlMethods) {
                    XposedBridge.hookMethod(it, nullHooker)
                }
            }
//            MethodFinder.fromClass(telecomLogClazz).onEach {
//                if (it.name in telecomLogBlMethods) {
//                    XposedBridge.hookMethod(it, nullHooker)
//                }
//            }
        }
    }

    private fun doDeoptimize() {
        for ((k, v) in deoptMap) {
            val clazz = getDefaultCL().loadClass(k)
            if (v[0] == "<all>") {
                Deoptimizer.deoptimizeAllMethods(clazz)
                continue
            }
            Deoptimizer.deoptimizeMethods(clazz, *v)
        }
    }
}
