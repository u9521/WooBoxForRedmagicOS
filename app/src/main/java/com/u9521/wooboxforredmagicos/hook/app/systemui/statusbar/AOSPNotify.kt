package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import cn.fkj233.ui.activity.dp2px
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object AOSPNotify : HookRegister() {
    override fun init() {
        val iconSize = XSPUtils.getInt("aosp_icon_size_dp", 16)
        hasEnable("use_aosp_notify") {
            val adaptClazz = ClassUtils.loadClass("com.zte.base.Adapt")
            val adaptHooker: XC_MethodHook = object : XC_MethodHook() {
                val enableAdapt = adaptClazz.getDeclaredField("ZTE_STYLE")

                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    enableAdapt.setBoolean(param.thisObject, false)
                }

                override fun afterHookedMethod(param: MethodHookParam) {
                    enableAdapt.setBoolean(param.thisObject, true)
                }
            }
            val notificationBGClazz =
                ClassUtils.loadClass("com.zte.feature.notification.module.NotificationBackground")
            // Force all app use system icon
            MethodFinder.fromClass("com.zte.feature.notification.NotificationUtil")
                .filterByName("shouldShowAppIcon").first().createBeforeHook {
                    it.result = false
                }
            // clean app icon background;fix sometime icon with background
            MethodFinder.fromClass("com.zte.feature.notification.NotificationUtil")
                .filterByName("getAppIconBackgroundDrawable").first().createBeforeHook {
                    it.result = null
                }

            MethodFinder.fromClass(notificationBGClazz).filterByName("adjustNotificationIcon")
                .first().createBeforeHook {
                    val view = it.args[0] as View
                    val iconSizePx = dp2px(view.context, iconSize.toFloat())
                    val lp = FrameLayout.LayoutParams(iconSizePx, iconSizePx)
                    lp.gravity = Gravity.CENTER_HORIZONTAL
                    view.layoutParams = lp
                    it.result = null
//                    Log.wx("WTF: this method shouldnot be called, stacktrace bellow")
//                    DebugUtils.printStackTrace()
                }
            val nICAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationIconContainerAdapt")
            val nIACAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationIconAreaControllerAdapt")
            val nHVWAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationHeaderViewWrapperAdapt")
            val nTVWAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationTemplateViewWrapperAdapt")
            val eNRAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.ExpandableNotificationRowAdapt")
            MethodFinder.fromClass(nHVWAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(nICAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(nTVWAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(eNRAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(nIACAdapt).onEach { it ->
                if (it.name == "adjustNotificationIcon") {
                    return@onEach
                }
                XposedBridge.hookMethod(it, adaptHooker)
            }
        }
    }
}