package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object NoVibrateForvolKeyLongpress : HookRegister() {
    override fun init() = hasEnable("no_vibrate_volKey_Long_press") {
        MethodFinder.fromClass("com.zte.adapt.mifavor.volume.VolumeDialogImplAdapt")
            .filterByName("richTapVibrateForVolumeKeyLongPress").filterEmptyParam().first()
            .createBeforeHook {
                it.result = null
            }
    }
}