package com.u9521.wooboxforredmagicos.hook.app.systemui.features

import android.view.WindowManager
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object AOSPSingleHandModeAdjust : HookRegister() {
    override fun init() {
        hasEnable("aosp_singlehandmode_adjust") {
            val oHDAOClazz =
                ClassUtils.loadClass("com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer")
            val oHTHClazz =
                ClassUtils.loadClass("com.android.wm.shell.onehanded.OneHandedTutorialHandler")
            val uppx = XSPUtils.getInt("aosp_singlehandmode_offest", 0)
            MethodFinder.fromClass(oHDAOClazz).filterByName("scheduleOffset").first()
                .createBeforeHook {
                    val rollDownpx = it.args[1] as Int
                    //raise some px on onehand mode also can bypass statusbar restrict,dont know why works
                    if (rollDownpx - uppx > 0 && rollDownpx != 0) {
                        it.args[1] = rollDownpx - uppx
                    }
                }
            //also adjust tutorial
            MethodFinder.fromClass(oHTHClazz).filterByName("getTutorialTargetLayoutParams").first()
                .createAfterHook {
                    val tTlp = it.result as WindowManager.LayoutParams
                    tTlp.apply {
                        if (height - uppx > 0) {
                            height -= uppx
                        }
                    }
                    it.result = tTlp
                }
        }
    }
}
