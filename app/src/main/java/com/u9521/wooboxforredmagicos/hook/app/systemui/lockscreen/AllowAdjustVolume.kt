package com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object AllowAdjustVolume : HookRegister() {
    override fun init() = hasEnable("lockscreen_allow_adjust_volume") {
        MethodFinder.fromClass("com.zte.feature.volume.MfvVolumeDialog")
            .filterByName("shouldKeyguardHandleVolumeKeys").filterEmptyParam().first()
            .createBeforeHook {
                it.result = false
            }

    }
}