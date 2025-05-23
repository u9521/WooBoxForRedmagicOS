package com.u9521.wooboxforredmagicos.hook.app.systemui.statusbar

import android.annotation.SuppressLint
import android.view.View
import android.widget.FrameLayout
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object HideMobileActivityIcon : HookRegister() {
    override fun init() = hasEnable("hide_mobile_activity_icon") {
        //隐藏移动数据箭头
        val adaptClazz =
            getDefaultCL().loadClass("com.zte.adapt.mifavor.signal.StatusBarMobileViewAdapt")
        val updateMe =
            adaptClazz.getDeclaredMethod("updateActivityType", Int::class.javaPrimitiveType)
        val updateMeHooker = object : XC_MethodHook() {
            @SuppressLint("PrivateApi", "DiscouragedApi")
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                val cAClazz = getDefaultCL().loadClass("com.zte.base.ClassAdaptor")
                val sBMVClazz =
                    getDefaultCL().loadClass("com.android.systemui.statusbar.StatusBarMobileView")
                val adaptObj = cAClazz.getDeclaredField("mOwner").get(param!!.thisObject)
                val sbmvObj = sBMVClazz.cast(adaptObj) as FrameLayout
                sbmvObj.findViewById<View>(
                    sbmvObj.context.resources.getIdentifier(
                        "data_inout", "id", getLoadPackageParam().packageName
                    )
                ).visibility = View.GONE
            }
        }
        XposedBridge.hookMethod(updateMe, updateMeHooker)
    }
}