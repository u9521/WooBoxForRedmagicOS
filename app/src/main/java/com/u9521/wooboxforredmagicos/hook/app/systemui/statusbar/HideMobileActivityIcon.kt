package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.view.View
import android.widget.FrameLayout
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.github.kyuubiran.ezxhelper.misc.ViewUtils.findViewByIdName
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HideMobileActivityIcon : HookRegister() {
    override fun init() = hasEnable("hide_mobile_activity_icon") {
        //隐藏移动数据箭头
        MethodFinder.fromClass("com.zte.adapt.mifavor.signal.StatusBarMobileViewAdapt")
            .filterByName("updateActivityType").first().createAfterHook {
                val CAClazz = ClassUtils.loadClass("com.zte.base.ClassAdaptor")
                val SBMVClazz =
                    ClassUtils.loadClass("com.android.systemui.statusbar.StatusBarMobileView")
                val adaptobj = CAClazz.getDeclaredField("mOwner").get(it.thisObject)
                val SbmvObj = SBMVClazz.cast(adaptobj) as FrameLayout
                SbmvObj.findViewByIdName("data_inout")!!.visibility =
                    View.GONE
            }
    }
}