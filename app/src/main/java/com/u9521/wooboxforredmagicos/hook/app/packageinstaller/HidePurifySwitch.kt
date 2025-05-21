package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object HidePurifySwitch : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("hide_purify_switch") {
        val uICookToolClzz =
            getDefaultClassLoader().loadClass("com.android.packageinstaller.PackageInstallerActivity\$UICookTool")
        val packageInstallerActivityclzz =
            getDefaultClassLoader().loadClass("com.android.packageinstaller.PackageInstallerActivity")
        val getCookUIMe = packageInstallerActivityclzz.getDeclaredMethod(
            "getCookUI",
            //dangerlevel 0:unknown 1:Secure .. 3:high Risk
            Int::class.javaPrimitiveType,
            //defraudlevel0
            Int::class.javaPrimitiveType,
            //defraudlevel1 ICP备案信息，1没有，2违规
            Int::class.javaPrimitiveType,
            //puremode
            Boolean::class.javaPrimitiveType,
            //virusName
            String::class.java
        )
        val cookUIHooker = object : XC_MethodHook() {
            override fun afterHookedMethod(param: MethodHookParam?) {
                super.afterHookedMethod(param)
                Log.i("trying to remove PurifySwitch")
                val uICookToolOBJ = uICookToolClzz.cast(param!!.result)
                uICookToolClzz.getDeclaredField("cleanBgColor").setBoolean(uICookToolOBJ, true)
                uICookToolClzz.getDeclaredField("hidePureModeSwitchLayout")
                    .setBoolean(uICookToolOBJ, true)
                uICookToolClzz.getDeclaredField("hideWarningLayout").setBoolean(uICookToolOBJ, true)
                uICookToolClzz.getDeclaredField("needIsolate").setBoolean(uICookToolOBJ, false)
                uICookToolClzz.getDeclaredField("showRiskCheckbox").setBoolean(uICookToolOBJ, false)
                uICookToolClzz.getDeclaredField("showSwlimitLearnMore")
                    .setBoolean(uICookToolOBJ, false)
                uICookToolClzz.getDeclaredField("warningTitle").set(uICookToolOBJ, "彩蛋")
                uICookToolClzz.getDeclaredField("warningUnknownText")
                    .set(uICookToolOBJ, "这是一条提醒")
                param.result = uICookToolOBJ
            }
        }
        XposedBridge.hookMethod(getCookUIMe, cookUIHooker)
    }
}