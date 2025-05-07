package com.u9521.wooboxforredmagicos.hook.app.nfcService

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object MuteNfcSound : HookRegister() {
    override fun init() = hasEnable("mute_nfc_sound") {
        MethodFinder.fromClass("com.android.nfc.NfcService").filterByName("playSound").first()
            .createBeforeHook {
                it.result = null
            }
    }
}