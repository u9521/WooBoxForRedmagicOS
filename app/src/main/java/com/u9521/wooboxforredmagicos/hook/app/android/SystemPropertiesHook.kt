package com.u9521.wooboxforredmagicos.hook.app.android


import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object SystemPropertiesHook : HookRegister() {
    override fun init() {
        return
//        val mediaStepsSwitch = XSPUtils.getBoolean("media_volume_steps_switch", false)
//        val mediaSteps = XSPUtils.getInt("media_volume_steps", 30)
//
//        findMethod("android.os.SystemProperties") {
//            name == "getInt" && returnType == Int::class.java
//        }.hookBefore {
//            when (it.args[0] as String) {
//                "ro.config.media_vol_steps" -> if (mediaStepsSwitch) it.result = mediaSteps
//            }
//        }
    }
}