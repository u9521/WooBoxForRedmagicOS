package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.android.*
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object Android : AppRegister() {
    override val packageName: List<String> = listOf("android")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox-System"
    override val loadDexkit: Boolean = false

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {

        DisableFlagSecure().handleLoadPackage(lpparam) //允许截图
        autoInitHooks(
            lpparam,
            AirplaneMode,//飞行模式设置
            BlockScreenOnNotificationSound,//屏蔽亮屏通知声音和振动
            WLANServerHooker,//WLAN服务，国家码，mac地址
            RemoveAlertWindowsNotification, //上层显示通知
            RmIntentHijack,//去除安装器劫持
            VolumeStepHook, //音量阶数Hook
            AllowUntrustedTouches, //允许不受信任的触摸
            RmWindowReplyLimits,//解除小窗限制
            SyetemServerLogBlocker,//屏蔽一些系统日志
            PowerWakeupAssist,//长按电源键打开默认数字助理
        )
    }


}