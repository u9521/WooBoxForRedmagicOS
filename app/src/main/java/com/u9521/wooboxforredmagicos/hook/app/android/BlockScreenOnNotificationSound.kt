package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import android.app.KeyguardManager
import android.content.Context
import android.media.AudioManager
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object BlockScreenOnNotificationSound : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {

        val nrClazz =
            getDefaultCL().loadClass("com.android.server.notification.NotificationRecord")
        val nMSClazz =
            getDefaultCL().loadClass("com.android.server.notification.NotificationManagerService")

        val audioManager = nMSClazz.getDeclaredField("mAudioManager")
        val screenOn = nMSClazz.getDeclaredField("mScreenOn")

        val bBBMe =
            nMSClazz
                .getDeclaredMethod("buzzBeepBlinkLocked", nrClazz)
        val getContext = nMSClazz.superclass.getDeclaredMethod("getContext")

        val screenOnNotificationHooker = object : XC_MethodHook() {
            var screenOnBlock = false

            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                screenOnBlock = XSPUtils.getBoolean("block_notification_sound_screen_on", false)
                val context = getContext.invoke(param!!.thisObject) as Context
                val km = context
                    .getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                if (screenOnBlock && screenOn.getBoolean(param.thisObject) && !km.isKeyguardLocked) {
//                    Log.e("亮屏消息来咯")
                    val am = audioManager.get(param.thisObject) as AudioManager?
                    if (am != null) {
                        audioManager.set(param.thisObject, null)
                    }
                }
            }

            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                if (audioManager.get(param!!.thisObject) == null) {
                    val am =
                        (getContext.invoke(param.thisObject) as Context).getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    audioManager.set(param.thisObject, am)
                }
            }
        }
        XposedBridge.hookMethod(bBBMe, screenOnNotificationHooker)
        // fixed This
        // Attempt to invoke virtual method 'android.media.IRingtonePlayer android.media.AudioManager.getRingtonePlayer()' on a null object reference
        val clrSoundLockMe = nMSClazz.getDeclaredMethod("clearSoundLocked")
        val clrSoundLockHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val getAm = audioManager.get(param!!.thisObject) as AudioManager?
                if (getAm == null) {
//                    Log.ix("BlockScreenOnNotificationSound: restore mAudioManager")
                    val am =
                        (getContext.invoke(param.thisObject) as Context).getSystemService(Context.AUDIO_SERVICE) as AudioManager
                    audioManager.set(param.thisObject, am)
                }
            }
        }
        XposedBridge.hookMethod(clrSoundLockMe, clrSoundLockHooker)
    }
}
