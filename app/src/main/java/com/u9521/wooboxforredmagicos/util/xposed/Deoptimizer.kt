package com.u9521.wooboxforredmagicos.util.xposed

import de.robv.android.xposed.XposedBridge
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Member
import java.lang.reflect.Method

object Deoptimizer {

    @JvmStatic
    var m: Method? = null

    @JvmStatic
    var deoptimizeMethod = m

    init {
        try {
            //noinspection JavaReflectionMemberAccess
            m = XposedBridge::class.java.getDeclaredMethod(
                "deoptimizeMethod", Member::class.java
            )
        } catch (t: Throwable) {
            XposedBridge.log(t)
        }
        deoptimizeMethod = m
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun deoptimizeMethod(c: Class<*>, methodName: String) {
        for (m in c.declaredMethods) {
            if (deoptimizeMethod != null && m.name == methodName) {
                deoptimizeMethod!!.invoke(null, m)
                Log.i("Method deoptimized: $m", logInRelease = true)
            }
        }
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun deoptimizeMethods(c: Class<*>, vararg methodName: String) {
        for (name in methodName) {
            deoptimizeMethod(c, name)
        }
    }

    @Throws(InvocationTargetException::class, IllegalAccessException::class)
    fun deoptimizeAllMethods(c: Class<*>) {
        val meArray = arrayOf<String>()
        for (m in c.declaredMethods) {
            meArray.plus(m.name)
        }
        deoptimizeMethods(c, *meArray)
    }

}