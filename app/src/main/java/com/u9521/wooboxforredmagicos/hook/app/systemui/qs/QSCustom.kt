package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.content.res.Configuration
import android.view.ViewGroup
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.EzXHelper
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object QSCustom : HookRegister() {
    override fun init() = hasEnable("qs_custom_switch") {
        val mRows = XSPUtils.getInt("qs_custom_rows", 4)
        val mRowsLandscape = XSPUtils.getInt("qs_custom_rows_landscape", 1)
        val mColumns = XSPUtils.getInt("qs_custom_columns", 5)
        val mColumnsLandscape = XSPUtils.getInt("qs_custom_columns_landscape", 7)
        val mColumnsEditor = XSPUtils.getInt("qs_custom_columns_editor", 5)
        val mColumnsLandscapeEditor = XSPUtils.getInt("qs_custom_columns_landscape_editor", 6)

        val mfvTileLayoutAdaptClazz =
            ClassUtils.loadClass("com.zte.adapt.mifavor.qs.MfvTileLayoutAdapt")
        val mfvTileLayoutClazz =
            ClassUtils.loadClass("com.zte.mifavor.qs.MfvTileLayout")
        //fix animation
        // hook com.zte.mifavor.qs.MfvQSAnimator updateViews will cause race condition
        MethodFinder.fromClass("com.zte.utils.QsDimenUtils\$Companion")
            .filterByName("getTileColumns").first().createBeforeHook {
                val orientation = EzXHelper.appContext.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    it.result = mColumns
                } else {
                    it.result = mColumnsLandscape
                }
            }
        // weird but must handle this, otherwise the tiles will be misaligned
        //editor mode
        MethodFinder.fromClass("com.zte.feature.qs.layout.QsCustomizerModule")
            .filterByName("getColumns").first().createBeforeHook {
                val orientation = EzXHelper.appContext.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    it.result = mColumnsEditor
                } else {
                    it.result = mColumnsLandscapeEditor
                }
            }

        //Columns
        MethodFinder.fromClass(mfvTileLayoutAdaptClazz).filterByName("getTileColumns").first()
            .createBeforeHook {
                val orientation = EzXHelper.appContext.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    it.result = mColumns
                } else {
                    it.result = mColumnsLandscape
                }
            }
        //max rows
        MethodFinder.fromClass(mfvTileLayoutAdaptClazz).filterByName("getRows").first()
            .createBeforeHook {
                val orientation = EzXHelper.appContext.resources.configuration.orientation
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    it.result = mRows
                } else {
                    it.result = mRowsLandscape
                }
            }
        // froce update row
        MethodFinder.fromClass(mfvTileLayoutClazz).filterByName("updateMaxRows").first()
            .createAfterHook {
                val viewGroup = it.thisObject as ViewGroup
                val orientation = viewGroup.context.resources.configuration.orientation
                val orimRows =
                    mfvTileLayoutClazz.getDeclaredField("mRows").getInt(it.thisObject)
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    mfvTileLayoutClazz.getDeclaredField("mRows").setInt(it.thisObject, mRows)
                    it.result = orimRows != mRows
                } else {
                    mfvTileLayoutClazz.getDeclaredField("mRows")
                        .setInt(it.thisObject, mRowsLandscape)
                    it.result = orimRows != mRowsLandscape
                }
            }
    }
}