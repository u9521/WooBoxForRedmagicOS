package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
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
        hasEnable("use_aosp_notify") {
            val adaptClazz = ClassUtils.loadClass("com.zte.base.Adapt")
            var adaptHooker: XC_MethodHook = object : XC_MethodHook() {
                val enableAdapt = adaptClazz.getDeclaredField("ZTE_STYLE")

                @Throws(Throwable::class)
                override fun beforeHookedMethod(param: MethodHookParam) {
                    enableAdapt.setBoolean(param.thisObject, false)
                }

                override fun afterHookedMethod(param: MethodHookParam?) {
                    enableAdapt.setBoolean(param!!.thisObject, true)
                }
            }

            val CBGMethod =
                MethodFinder.fromClass("com.zte.feature.notification.module.NotificationBackground")
                    .filterByName("clearBackground").first()
            MethodFinder.fromClass("com.zte.feature.notification.NotificationUtil")
                .filterByName("shouldShowAppIcon").first().createBeforeHook {
                    it.result = false
                }
            MethodFinder.fromClass("com.zte.feature.notification.module.NotificationBackground")
                .filterByName("updateNotificationIconBackground").first().createBeforeHook {
                    CBGMethod.invoke(it.thisObject)
                }
            val NICAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationIconContainerAdapt")
            val NIACAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationIconAreaControllerAdapt")
            val NHVWAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationHeaderViewWrapperAdapt")
            val NTVWAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.NotificationTemplateViewWrapperAdapt")
            val ENRAdapt =
                ClassUtils.loadClass("com.zte.adapt.mifavor.notification.ExpandableNotificationRowAdapt")
            MethodFinder.fromClass(NHVWAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(NICAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(NTVWAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(ENRAdapt).onEach {
                XposedBridge.hookMethod(it, adaptHooker)
            }
            MethodFinder.fromClass(NIACAdapt).onEach { it ->
                if (it.name == "adjustNotificationIcon") {
                    it.createBeforeHook {
                        var iconSize =
                            XSPUtils.getInt("aosp_icon_size_dp", 16)
                        val view = it.args[0] as View
                        iconSize =
                            (iconSize * view.context.resources.displayMetrics.density).toInt()
                        val lp = FrameLayout.LayoutParams(
                            iconSize,
                            iconSize
                        )
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