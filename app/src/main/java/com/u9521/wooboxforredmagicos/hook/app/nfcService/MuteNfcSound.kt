package com.u9521.wooboxforredmagicos.hook.app.nfcService

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object MuteNfcSound : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("mute_nfc_sound") {
        val pSMe =
            getDefaultCL().loadClass("com.android.nfc.NfcService").getDeclaredMethod(
                "playSound",
                Int::class.javaPrimitiveType
            )
        val pSHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                Log.i("blocked NFC Sound")
                param!!.result = null
            }
        }
        XposedBridge.hookMethod(pSMe, pSHooker)
    }
}