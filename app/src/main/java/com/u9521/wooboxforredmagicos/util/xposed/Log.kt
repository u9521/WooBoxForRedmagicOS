package com.u9521.wooboxforredmagicos.util.xposed

import com.u9521.wooboxforredmagicos.BuildConfig.DEBUG
import de.robv.android.xposed.XposedBridge

@Suppress("KotlinConstantConditions")
object Log {
//    @JvmStatic
//    lateinit var XposedModule: XposedModule

    @JvmStatic
    lateinit var logTag: String
    var isDebug = DEBUG

    fun px(level: String, msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        val logMessage = buildString {
            append("[$level/$logTag] ")
            append(msg)
        }
        if (thr != null) {
            XposedBridge.log(logMessage)
            XposedBridge.log(thr)
            return
        }
        XposedBridge.log(logMessage)
    }

    @JvmStatic
    fun i(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        android.util.Log.i(logTag, msg, thr)
    }

    @JvmStatic
    fun d(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        android.util.Log.d(logTag, msg, thr)
    }

    @JvmStatic
    fun w(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        android.util.Log.w(logTag, msg, thr)
    }

    @JvmStatic
    fun e(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        android.util.Log.e(logTag, msg, thr)
    }

    @JvmStatic
    fun wtf(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        if (!isDebug && !logInRelease) {
            return
        }
        android.util.Log.wtf(logTag, msg, thr)
    }

    @JvmStatic
    fun ix(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        i(msg, thr, logInRelease)
        px("I", msg, thr, logInRelease)
    }

    @JvmStatic
    fun wx(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        w(msg, thr, logInRelease)
        px("I", msg, thr, logInRelease)
    }

    @JvmStatic
    fun dx(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        d(msg, thr, logInRelease)
        px("I", msg, thr, logInRelease)
    }

    @JvmStatic
    fun ex(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        e(msg, thr, logInRelease)
        px("I", msg, thr, logInRelease)
    }

    @JvmStatic
    fun wtfx(msg: String, thr: Throwable? = null, logInRelease: Boolean = false) {
        wtf(msg, thr, logInRelease)
        px("WTF", msg, thr, logInRelease)
    }

}