package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.content.res.Configuration
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object QSCustom : HookRegister() {
    override fun init() = hasEnable("qs_custom_switch") {
        val mRows = XSPUtils.getInt("qs_custom_rows", 4)
        val mRowsLandscape = XSPUtils.getInt("qs_custom_rows_landscape", 1)
        val mColumns = XSPUtils.getInt("qs_custom_columns", 5)
        val mColumnsLandscape = XSPUtils.getInt("qs_custom_columns_landscape", 6)

        val mfvTileLayoutClazz = ClassUtils.loadClass("com.zte.mifavor.qs.MfvTileLayout")
        MethodFinder.fromClass(mfvTileLayoutClazz).filterByName("updateColumns").first()
            .createAfterHook {
                val viewGroup = it.thisObject as ViewGroup
                val orimColumns =
                    mfvTileLayoutClazz.getDeclaredField("mColumns").getInt(it.thisObject)
                val orientation = viewGroup.context.resources.configuration.orientation
                if (viewGroup.javaClass.name.equals("com.zte.controlcenter.view.ControlCenterTileLayout")) {
                    if (orimColumns == 2 && orientation == Configuration.ORIENTATION_PORTRAIT || orimColumns == 4 && orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        //not suitable for separate layout
                        return@createAfterHook
                    }
                }
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mfvTileLayoutClazz.getDeclaredField("mColumns").setInt(it.thisObject, mColumns)
                    it.result = orimColumns != mColumns
                } else {
                    mfvTileLayoutClazz.getDeclaredField("mColumns")
                        .setInt(it.thisObject, mColumnsLandscape)
                    it.result = orimColumns != mColumnsLandscape
                }
            }

        MethodFinder.fromClass(mfvTileLayoutClazz).filterByName("updateResources").first()
            .createAfterHook {
                val viewGroup = it.thisObject as ViewGroup
                val orientation = viewGroup.context.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mfvTileLayoutClazz.getDeclaredField("mMaxAllowedRows")
                        .setInt(it.thisObject, mRows)
                } else {
                    mfvTileLayoutClazz.getDeclaredField("mMaxAllowedRows")
                        .setInt(it.thisObject, mRowsLandscape)
                }
                viewGroup.requestLayout()
            }
        MethodFinder.fromClass(mfvTileLayoutClazz).filterByName("updateMaxRows").first()
            .createAfterHook {
                val viewGroup = it.thisObject as ViewGroup
                val orientation = viewGroup.context.resources.configuration.orientation
                val orimRows = mfvTileLayoutClazz.getDeclaredField("mRows").getInt(it.thisObject)
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mfvTileLayoutClazz.getDeclaredField("mRows").setInt(it.thisObject, mRows)
                    it.result = orimRows != mRows
                } else {
                    mfvTileLayoutClazz.getDeclaredField("mRows")
                        .setInt(it.thisObject, mRowsLandscape)
                    it.result = orimRows != mRowsLandscape
                }
            }
        //fix animation
        val columnsHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                val orientation = EzXHelper.appContext.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    param!!.result = mColumns
                } else {
                    param!!.result = mColumnsLandscape
                }
            }

        }
        //separate layout
        MethodFinder.fromClass("com.zte.controlcenter.animation.ControlCenterPanelAnimator")
            .filterByName("updateViews").first().createHook {
                var hooker: XC_MethodHook.Unhook? = null
                before {
                    hooker = XposedBridge.hookMethod(
                        MethodFinder.fromClass("com.zte.utils.QsDimenUtils\$Companion")
                            .filterByName("getTileColumns").first(), columnsHooker
                    )
                }
                after {
                    hooker!!.unhook()
                }
            }
        //aosp layout
        MethodFinder.fromClass("com.zte.mifavor.qs.MfvQSAnimator")
            .filterByName("updateViews").first().createHook {
                var hooker: XC_MethodHook.Unhook? = null
                before {
                    hooker = XposedBridge.hookMethod(
                        MethodFinder.fromClass("com.zte.utils.QsDimenUtils\$Companion")
                            .filterByName("getTileColumns").first(), columnsHooker
                    )
                }
                after {
                    hooker!!.unhook()
                }
            }
    }
}