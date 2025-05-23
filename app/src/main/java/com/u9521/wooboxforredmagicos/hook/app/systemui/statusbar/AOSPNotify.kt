package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.content.pm.ApplicationInfo
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import cn.fkj233.ui.activity.dp2px
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object AOSPNotify : HookRegister() {
    override fun init() {
        val iconSize = XSPUtils.getInt("aosp_icon_size_dp", 16)
        hasEnable("use_aosp_notify") {
            val adaptClazz = getDefaultCL().loadClass("com.zte.base.Adapt")

            val adaptHooker: XC_MethodHook = object : XC_MethodHook() {
                val enableAdapt = adaptClazz.getDeclaredField("ZTE_STYLE")

                override fun beforeHookedMethod(param: MethodHookParam) {
                    enableAdapt.setBoolean(param.thisObject, false)
                }

                override fun afterHookedMethod(param: MethodHookParam) {
                    enableAdapt.setBoolean(param.thisObject, true)
                }
            }
            val notificationBGClazz =
                getDefaultCL().loadClass("com.zte.feature.notification.module.NotificationBackground")
            // Force all app use system icon
            val notificationUtilClazz =
                getDefaultCL().loadClass("com.zte.feature.notification.NotificationUtil")

            val showAppIconMe = notificationUtilClazz.getDeclaredMethod(
                "shouldShowAppIcon", ApplicationInfo::class.java
            )

            val getAppBGMe = notificationUtilClazz.getDeclaredMethod(
                "getAppIconBackgroundDrawable",
                String::class.java,
                Boolean::class.javaPrimitiveType
            )

            val showAppIconHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = false
                }
            }
            // clean app icon background;fix sometime icon with background
            val getAppBGMeHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = null
                }
            }
            XposedBridge.hookMethod(showAppIconMe, showAppIconHooker)
            XposedBridge.hookMethod(getAppBGMe, getAppBGMeHooker)
            val adjIconMe =
                notificationBGClazz.getDeclaredMethod("adjustNotificationIcon", View::class.java)
            val notificationIconHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val view = param!!.args[0] as View
                    val iconSizePx = dp2px(view.context, iconSize.toFloat())
                    val lp = FrameLayout.LayoutParams(iconSizePx, iconSizePx)
                    lp.gravity = Gravity.CENTER_HORIZONTAL
                    view.layoutParams = lp
                    param.result = null
                }
            }
            XposedBridge.hookMethod(adjIconMe, notificationIconHooker)

            val nICAdapt =
                getDefaultCL().loadClass("com.zte.adapt.mifavor.notification.NotificationIconContainerAdapt")
            val nIACAdapt =
                getDefaultCL().loadClass("com.zte.adapt.mifavor.notification.NotificationIconAreaControllerAdapt")
            val nHVWAdapt =
                getDefaultCL().loadClass("com.zte.adapt.mifavor.notification.NotificationHeaderViewWrapperAdapt")
            val nTVWAdapt =
                getDefaultCL().loadClass("com.zte.adapt.mifavor.notification.NotificationTemplateViewWrapperAdapt")
            val eNRAdapt =
                getDefaultCL().loadClass("com.zte.adapt.mifavor.notification.ExpandableNotificationRowAdapt")

            nHVWAdapt.declaredMethods.onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            nICAdapt.declaredMethods.onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            nTVWAdapt.declaredMethods.onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            eNRAdapt.declaredMethods.onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            nIACAdapt.declaredMethods.onEach {
                if (it.name == "adjustNotificationIcon") {
                    return@onEach
                }
                XposedBridge.hookMethod(it, adaptHooker)
            }
        }
    }
}