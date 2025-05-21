package com.u9521.wooboxforredmagicos.hook.app.android


import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object VolumeStepHook : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val alarmStepsSwitch = XSPUtils.getBoolean("alarm_volume_steps_switch", false)
        val alarmSteps = XSPUtils.getInt("alarm_volume_steps", 15)
        val mediaStepsSwitch = XSPUtils.getBoolean("media_volume_steps_switch", false)
        val mediaSteps = XSPUtils.getInt("media_volume_steps", 15)
        val notifyStepsSwitch = XSPUtils.getBoolean("notify_volume_steps_switch", false)
        val notifySteps = XSPUtils.getInt("notify_volume_steps", 15)
        val ringStepsSwitch = XSPUtils.getBoolean("ring_volume_steps_switch", false)
        val ringSteps = XSPUtils.getInt("ring_volume_steps", 15)
        val vcCallSwitch = XSPUtils.getBoolean("vc_call_volume_steps_switch", false)
        val vcCallSteps = XSPUtils.getInt("vc_call_volume_steps", 10)

        val propGetIntHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                when (param!!.args[0] as String) {
                    "ro.config.alarm_vol_steps" -> if (alarmStepsSwitch) param.result = alarmSteps
                    // zte specific
                    "ro.config.media_vol_steps" -> if (mediaStepsSwitch) param.result =
                        mediaSteps * 10

                    "ro.config.notify_vol_steps" -> if (notifyStepsSwitch) param.result =
                        notifySteps

                    "ro.config.ring_vol_steps" -> if (ringStepsSwitch) param.result = ringSteps

                    "ro.config.vc_call_vol_steps" -> if (vcCallSwitch) param.result = vcCallSteps
                }
            }
        }

        val propGetIntMe = getDefaultCL().loadClass("android.os.SystemProperties")
            .getDeclaredMethod("getInt", String::class.java, Int::class.javaPrimitiveType)


        val audioServerHooker = object : XC_MethodHook() {
            var audioServerUnhook: Unhook? = null
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("audioServer initializing, create Properties hook", logInRelease = true)
                audioServerUnhook = XposedBridge.hookMethod(propGetIntMe, propGetIntHooker)
            }

            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                audioServerUnhook!!.unhook()
                Log.i("audioServer initialized, released Properties hook", logInRelease = false)
            }
        }
        val audioServerConstructor =
            getDefaultCL().loadClass("com.android.server.audio.AudioService").constructors.first { it.parameterCount == 8 }
        XposedBridge.hookMethod(audioServerConstructor, audioServerHooker)

    }
}