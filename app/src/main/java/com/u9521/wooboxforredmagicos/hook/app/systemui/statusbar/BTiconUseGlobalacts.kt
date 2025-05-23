package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object BTiconUseGlobalacts : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("status_bar_bluetooth_icon_acts_global") {
        val btIconName = "bluetooth"
        val btControllerClazz =
            getDefaultCL().loadClass("com.android.systemui.statusbar.policy.BluetoothController")
        val bTConnected = btControllerClazz.getDeclaredMethod("isBluetoothConnected")
        val iCClazz =
            getDefaultCL().loadClass("com.android.systemui.statusbar.phone.StatusBarIconControllerImpl")
        val setIconVisibilityMe = iCClazz.getDeclaredMethod(
                "setIconVisibility",
                String::class.java,
                Boolean::class.javaPrimitiveType,
            )


        val btPolicyFeature =
            getDefaultCL().loadClass("com.zte.feature.bluetooth.BluetoothPolicyFeature")
        val getBT = btPolicyFeature.getDeclaredMethod("getBluetooth")
        val getIC = btPolicyFeature.getDeclaredMethod("getIconController")

        val updateBTMe =
            btPolicyFeature.getDeclaredMethod("updateBluetoothWithLevelInner", Int::class.java)
        val updateBTMeHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val isbTConnect = bTConnected.invoke(getBT.invoke(param!!.thisObject))
                val ic = getIC.invoke(param.thisObject)
                setIconVisibilityMe.invoke(ic, btIconName, isbTConnect)
            }
        }
        XposedBridge.hookMethod(updateBTMe, updateBTMeHooker)
    }
}