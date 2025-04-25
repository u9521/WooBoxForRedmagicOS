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

                override fun afterHookedMethod(param: MethodHookParam?) {
                    enableAdapt.setBoolean(param!!.thisObject, true)
                }
            }

            val cBGMethod =
                MethodFinder.fromClass("com.zte.feature.notification.module.NotificationBackground")
                    .filterByName("clearBackground").first()
            MethodFinder.fromClass("com.zte.feature.notification.NotificationUtil")
                .filterByName("shouldShowAppIcon").first().createBeforeHook {
                    it.result = false
                }
            MethodFinder.fromClass("com.zte.feature.notification.module.NotificationBackground")
                .filterByName("updateNotificationIconBackground").first().createBeforeHook {
                    cBGMethod.invoke(it.thisObject)
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
                    it.createBeforeHook {
                        val view = it.args[0] as View
                        val iconSizePx = dp2px(view.context, iconSize.toFloat())
                        val lp = FrameLayout.LayoutParams(iconSizePx, iconSizePx)
                        lp.gravity = Gravity.CENTER_HORIZONTAL
                        view.layoutParams = lp
                        it.result = null
                    }
                    return@onEach
                }
                XposedBridge.hookMethod(it, adaptHooker)
            }
        }
    }
}