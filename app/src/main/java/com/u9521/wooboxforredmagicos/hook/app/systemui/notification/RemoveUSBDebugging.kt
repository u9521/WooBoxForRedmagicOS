package com.u9521.wooboxforredmagicos.hook.app.systemui.notification

import android.content.Context
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookReturnConstant
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object RemoveUSBDebugging : HookRegister() {
    override fun init() = hasEnable("remove_usb_debugging") {
        findMethod("com.oplusos.systemui.notification.usb.UsbService") {
            name == "isAdbDebugEnable" && parameterTypes[0] == Context::class.java
        }.hookReturnConstant(false)
    }
}