package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.systemui.features.AOSPSingleHandModeAdjust
import com.u9521.wooboxforredmagicos.hook.app.systemui.features.GestureStartDefaultDigitalAssist
import com.u9521.wooboxforredmagicos.hook.app.systemui.features.NoVibrateForvolKeyLongpress
import com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen.AllowAdjustVolume
import com.u9521.wooboxforredmagicos.hook.app.systemui.lockscreen.KSBFontRestore
import com.u9521.wooboxforredmagicos.hook.app.systemui.notification.RemoveDevModeIsOn
import com.u9521.wooboxforredmagicos.hook.app.systemui.notification.RemoveFinishedCharging
import com.u9521.wooboxforredmagicos.hook.app.systemui.notification.RemoveUSBDebugging
import com.u9521.wooboxforredmagicos.hook.app.systemui.qs.QSClockShowSecond
import com.u9521.wooboxforredmagicos.hook.app.systemui.qs.QSCustom
import com.u9521.wooboxforredmagicos.hook.app.systemui.qs.QSHeaderFontRestore
import com.u9521.wooboxforredmagicos.hook.app.systemui.qs.QSHeaderShortcut
import com.u9521.wooboxforredmagicos.hook.app.systemui.qs.QSHeaderShowControl
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.AOSPNotify
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.BTiconUseGlobalacts
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.BatteryIconAdjuster
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.CustomClock
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.HideMobileActivityIcon
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.HideVpnIcon
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.HideWifiActivityIcon
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.NetworkSpeedAdjuster
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.SBFontRestore
import com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar.StatusBarDoubleTapToSleep
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_LoadPackage

object SystemUI : AppRegister() {
    override val packageName: List<String> = listOf("com.android.systemui")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox"
    override val loadDexkit: Boolean = true

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("WooBox: 成功 Hook " + javaClass.simpleName)
        autoInitHooks(
            lpparam,
            //快速设置
            QSCustom, //快速设置自定义
            QSClockShowSecond, //下拉状态栏时钟显秒
            QSHeaderShortcut,//重定向日期打开日历
            QSHeaderShowControl,//显示搜索和运营商
            QSHeaderFontRestore,//时间日期图标字体恢复默认

            //状态栏
            StatusBarDoubleTapToSleep, //双击状态栏锁屏
            BatteryIconAdjuster,//隐藏电量百分比号,连接电源时显示充电图标
            NetworkSpeedAdjuster, //双排网速,网速秒刷
            HideWifiActivityIcon, //隐藏WIFI箭头
            HideMobileActivityIcon, //隐藏移动箭头
            CustomClock, //自定义时钟
            HideVpnIcon,//老版本隐藏VPN图标
            BTiconUseGlobalacts,//未连接设备时隐藏蓝牙图标
            AOSPNotify,//原生通知图标和通知样式
            SBFontRestore,//时钟字体恢复

            //通知类
            RemoveUSBDebugging, //移除USB调试已开启通知
            RemoveFinishedCharging, //移除已充满通知
            RemoveDevModeIsOn, //移除开发者模式已开启通知

            //特性
            GestureStartDefaultDigitalAssist,//手势打开默认数字助理
            NoVibrateForvolKeyLongpress,//禁用长按调音量振动
            AOSPSingleHandModeAdjust,//单手模式允许下拉通知栏

            //锁屏界面
            AllowAdjustVolume,//锁屏允许调整音量
            KSBFontRestore,//锁屏字体恢复，不要汉仪正圆了
        )
    }
}