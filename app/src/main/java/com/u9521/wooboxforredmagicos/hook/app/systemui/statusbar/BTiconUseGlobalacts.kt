package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object BTiconUseGlobalacts : HookRegister() {
    override fun init() = hasEnable("status_bar_bluetooth_icon_acts_global") {
        val adaptClazz = ClassUtils.loadClass("com.zte.base.Adapt")
        val globalStyleStr = "MFV_ABROAD_STYLE"
        MethodFinder.fromClass("com.zte.feature.bluetooth.BluetoothPolicyFeature")
            .filterByName("updateBluetoothWithLevelInner").filterByParamTypes(Int::class.java)
            .first().createHook {
                before {
                    ClassUtils.setStaticObject(adaptClazz, globalStyleStr, true)
                }
                after {
                    ClassUtils.setStaticObject(adaptClazz, globalStyleStr, false)
                }
            }

    }
}