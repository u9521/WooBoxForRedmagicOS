package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.app.AndroidAppHelper
import android.content.res.Configuration
import android.view.ViewGroup
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
        val mColumnsLandscape = XSPUtils.getInt("qs_custom_columns_landscape", 7)
        val mColumnsEditor = XSPUtils.getInt("qs_custom_columns_editor", 5)
        val mColumnsLandscapeEditor = XSPUtils.getInt("qs_custom_columns_landscape_editor", 6)

        val mfvTileLayoutAdaptClazz =
            getDefaultClassLoader().loadClass("com.zte.adapt.mifavor.qs.MfvTileLayoutAdapt")
        val mfvTileLayoutClazz =
            getDefaultClassLoader().loadClass("com.zte.mifavor.qs.MfvTileLayout")
        //fix animation
        // hook com.zte.mifavor.qs.MfvQSAnimator updateViews will cause race condition
        val getTileColumnsMe =
            getDefaultClassLoader().loadClass("com.zte.utils.QsDimenUtils\$Companion")
                .getDeclaredMethod("getTileColumns")
        val tileColumnsHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                val orientation =
                    AndroidAppHelper.currentApplication().resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    param.result = mColumns
                } else {
                    param.result = mColumnsLandscape
                }
            }
        }
        XposedBridge.hookMethod(getTileColumnsMe, tileColumnsHooker)
        // weird but must handle this, otherwise the tiles will be misaligned
        //editor mode
        val getColumnsMe =
            getDefaultClassLoader().loadClass("com.zte.feature.qs.layout.QsCustomizerModule")
                .getDeclaredMethod("getColumns")
        val editorColumnsHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val orientation =
                    AndroidAppHelper.currentApplication().resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    param.result = mColumnsEditor
                } else {
                    param.result = mColumnsLandscapeEditor
                }
            }
        }
        XposedBridge.hookMethod(getColumnsMe, editorColumnsHooker)

        //Columns

        val gTCME = mfvTileLayoutAdaptClazz.getDeclaredMethod(
            "getTileColumns",
            Int::class.javaPrimitiveType
        )
        XposedBridge.hookMethod(gTCME, tileColumnsHooker)
        //max rows
        val getRowsMe =
            mfvTileLayoutAdaptClazz.getDeclaredMethod("getRows", Int::class.javaPrimitiveType)
        val rowsHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam) {
                super.beforeHookedMethod(param)
                val orientation =
                    AndroidAppHelper.currentApplication().resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    param.result = mRows
                } else {
                    param.result = mRowsLandscape
                }
            }
        }
        XposedBridge.hookMethod(getRowsMe, rowsHooker)
        // froce update row
        val updateMaxRowsMe = mfvTileLayoutClazz.getDeclaredMethod(
            "updateMaxRows",
            Int::class.javaPrimitiveType,
            Int::class.javaPrimitiveType
        )
        val updateMaxRowsHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam) {
                super.afterHookedMethod(param)
                val viewGroup = param.thisObject as ViewGroup
                val orientation = viewGroup.context.resources.configuration.orientation
                val orimRows =
                    mfvTileLayoutClazz.getDeclaredField("mRows").getInt(param.thisObject)
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mfvTileLayoutClazz.getDeclaredField("mRows").setInt(param.thisObject, mRows)
                    param.result = orimRows != mRows
                } else {
                    mfvTileLayoutClazz.getDeclaredField("mRows")
                        .setInt(param.thisObject, mRowsLandscape)
                    param.result = orimRows != mRowsLandscape
                }
            }
        }
        XposedBridge.hookMethod(updateMaxRowsMe, updateMaxRowsHooker)
    }
}