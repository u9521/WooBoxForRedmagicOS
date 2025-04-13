package com.u9521.wooboxforredmagicos.hook.app.android


import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.ConstructorFinder
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook

object VolumeStepHook : HookRegister() {
    override fun init() {
        val alarmStepsSwitch = XSPUtils.getBoolean("alarm_volume_steps_switch", false)
        val alarmSteps = XSPUtils.getInt("alarm_volume_steps", 15)
        val mediaStepsSwitch = XSPUtils.getBoolean("media_volume_steps_switch", false)
        val mediaSteps = XSPUtils.getInt("media_volume_steps", 15)
        val notifyStepsSwitch = XSPUtils.getBoolean("notify_volume_steps_switch", false)
        val notifySteps = XSPUtils.getInt("notify_volume_steps", 15)
        val ringStepsSwitch = XSPUtils.getBoolean("ring_volume_steps_switch", false)
        val ringSteps = XSPUtils.getInt("ring_volume_steps", 15)
        val vc_callSwitch = XSPUtils.getBoolean("vc_call_volume_steps_switch", false)
        val vc_callSteps = XSPUtils.getInt("vc_call_volume_steps", 10)

        ConstructorFinder.fromClass("com.android.server.audio.AudioService")
            .first().createHook {
                var AudioServerHooker: XC_MethodHook.Unhook? = null
                before {
                    AudioServerHooker =
                        MethodFinder.fromClass("android.os.SystemProperties").filterByName("getInt")
                            .filterByReturnType(
                                Int::class.javaPrimitiveType!!
                            ).first().createBeforeHook {
                                when (it.args[0] as String) {
                                    "ro.config.alarm_vol_steps" -> if (alarmStepsSwitch) it.result =
                                        alarmSteps
                                    // zte specific
                                    "ro.config.media_vol_steps" -> if (mediaStepsSwitch) it.result =
                                        mediaSteps * 10

                                    "ro.config.notify_vol_steps" -> if (notifyStepsSwitch) it.result =
                                        notifySteps

                                    "ro.config.ring_vol_steps" -> if (ringStepsSwitch) it.result =
                                        ringSteps

                                    "ro.config.vc_call_vol_steps" -> if (vc_callSwitch) it.result =
                                        vc_callSteps
                                }
                            }
                }
                after {
                    AudioServerHooker!!.unhook()
                }
            }
    }
}