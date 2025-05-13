package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.android.AirplaneMode
import com.u9521.wooboxforredmagicos.hook.app.android.AllowUntrustedTouches
import com.u9521.wooboxforredmagicos.hook.app.android.DisableFlagSecure
import com.u9521.wooboxforredmagicos.hook.app.android.RemoveAlertWindowsNotification
import com.u9521.wooboxforredmagicos.hook.app.android.RmWindowReplyLimits
import com.u9521.wooboxforredmagicos.hook.app.android.SyetemServerLogBlocker
import com.u9521.wooboxforredmagicos.hook.app.android.VolumeStepHook
import com.u9521.wooboxforredmagicos.hook.app.android.WLANServerHooker
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android : AppRegister() {
    override val packageName: List<String> = listOf("android")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"
    override val loadDexkit: Boolean = false

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)

        DisableFlagSecure().handleLoadPackage(lpparam) //允许截图
        autoInitHooks(
            lpparam,
            AirplaneMode,//飞行模式设置
            WLANServerHooker,//WLAN服务，国家码，mac地址
            RemoveAlertWindowsNotification, //上层显示通知
            VolumeStepHook, //音量阶数Hook
            AllowUntrustedTouches, //允许不受信任的触摸
            RmWindowReplyLimits,//解除小窗限制
            SyetemServerLogBlocker,//屏蔽一些系统日志
        )
    }


}