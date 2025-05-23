package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import android.annotation.SuppressLint
import android.view.WindowManager
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import kotlin.math.ceil

object AOSPSingleHandModeAdjust : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("aosp_singlehandmode_adjust") {
            val oHDAOClazz =
                getDefaultCL().loadClass("com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer")
            val oHTHClazz =
                getDefaultCL().loadClass("com.android.wm.shell.onehanded.OneHandedTutorialHandler")
            val upOffsetPx = XSPUtils.getInt("aosp_singlehandmode_offest", 0)
            val scheduleOffsetMe =
                oHDAOClazz.getDeclaredMethod(
                    "scheduleOffset",
                    Int::class.javaPrimitiveType,
                    Int::class.javaPrimitiveType
                )
            val scheduleOffsetMeHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val rollDownPx = param!!.args[1] as Int
                    val finOffset = rollDownPx - upOffsetPx
                    if (rollDownPx == 0) {
                        return
                    }
                    //up overflow , down overflow 0.4 is actually 39.999996%,quit signlehand
                    if (finOffset <= 0 || finOffset > ceil(rollDownPx / 0.4)) {
                        Log.ex("singleHand screen overflow!! HeightOffset:$finOffset")
                        return
                    }
                    //raise some px on onehand mode also can bypass statusbar restrict,dont know why works
                    //this is a system bug ,fixed at RMOS 9.5.25
                    param.args[1] = rollDownPx - upOffsetPx
                }
            }
            XposedBridge.hookMethod(scheduleOffsetMe, scheduleOffsetMeHooker)
            //also adjust tutorial
            val getTutorialLPMe = oHTHClazz.getDeclaredMethod("getTutorialTargetLayoutParams")
            val getTutorialLPMeHooker = object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam?) {
                    super.afterHookedMethod(param)
                    val tTlp = param!!.result as WindowManager.LayoutParams
                    tTlp.apply {
                        if (height - upOffsetPx <= 0 || (height - upOffsetPx) > ceil(height / 0.4)) {
                            return
                        }
                        height -= upOffsetPx
                    }
                }
            }
            XposedBridge.hookMethod(getTutorialLPMe, getTutorialLPMeHooker)
        }
    }
}
