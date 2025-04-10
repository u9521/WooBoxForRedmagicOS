package com.u9521.wooboxforredmagicos.hook.app.launcher

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object ForceSupportResizeActivity : HookRegister() {
    override fun init() = hasEnable("launcher_Force_Support_ResizeActivity") {
        val aContextclazz = ClassUtils.loadClass("android.content.Context")
        val taskclazz = ClassUtils.loadClass("com.android.systemui.shared.recents.model.Task")
        MethodFinder.fromClass("com.android.quickstep.util.MiniWindowSplitScreenUtils")
            .filterByName("checkActivitySupportResize")
            .filterByParamTypes(
                aContextclazz,
                taskclazz
            ).first().createBeforeHook {
                it.result =true
                }
            }
    }