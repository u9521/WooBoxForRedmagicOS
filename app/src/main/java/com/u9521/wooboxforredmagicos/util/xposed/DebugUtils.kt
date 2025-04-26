package com.u9521.wooboxforredmagicos.util.xposed

import de.robv.android.xposed.XposedBridge

@Suppress("unused")
object DebugUtils {
    @JvmStatic
    fun printStackTrace() {
        // 获取当前线程的堆栈跟踪
        val stackTrace = Throwable().stackTrace
        // 格式化输出堆栈信息
        XposedBridge.log("Woobox Stacktrace Start")
        for (element in stackTrace) {
            XposedBridge.log(
                "Class: " + element.className +
                        ", Method: " + element.methodName +
                        ", Line: " + element.lineNumber
            )
        }
        XposedBridge.log("Woobox Stacktrace End")
    }
}
