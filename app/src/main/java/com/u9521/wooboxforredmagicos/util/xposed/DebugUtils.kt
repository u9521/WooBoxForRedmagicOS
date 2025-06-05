package com.u9521.wooboxforredmagicos.util.xposed

import de.robv.android.xposed.XposedBridge

@Suppress("unused")
object DebugUtils {
    @JvmStatic
    fun logStackTrace() {
        // 获取当前线程的堆栈跟踪
        val t = Throwable("Woobox StackTrace")
        Log.px("D", "Woobox Stacktrace Start", logInRelease = true)
        XposedBridge.log(t)
        Log.px("D", "Woobox Stacktrace End", logInRelease = true)
    }
}
