package com.u9521.wooboxforredmagicos.util.xposed

import de.robv.android.xposed.XposedBridge

@Suppress("unused")
object DebugUtils {
    @JvmStatic
    fun logStackTrace() {
        // 获取当前线程的堆栈跟踪
        val t = Throwable("Woobox StackTrace")
        XposedBridge.log("Woobox Stacktrace Start")
        XposedBridge.log(t)
        XposedBridge.log("Woobox Stacktrace End")
    }
}
