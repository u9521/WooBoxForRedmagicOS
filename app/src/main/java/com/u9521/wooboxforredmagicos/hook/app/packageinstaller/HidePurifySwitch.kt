package com.u9521.wooboxforredmagicos.hook.app.packageinstaller

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createAfterHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.ObjectHelper.Companion.objectHelper
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object HidePurifySwitch : HookRegister() {
    override fun init() = hasEnable("hide_purify_switch") {
        val uICookToolClzz =
            ClassUtils.loadClass("com.android.packageinstaller.PackageInstallerActivity\$UICookTool")
        val packageInstallerActivityclzz =
            ClassUtils.loadClass("com.android.packageinstaller.PackageInstallerActivity")
        MethodFinder.fromClass(packageInstallerActivityclzz)
            .filterByName("getCookUI").filterByReturnType(uICookToolClzz).first()
            .createAfterHook {
                Log.i("[packageinstaller]: trying to HidePurifySwitch")
                val uICookToolOBJ = it.result
                uICookToolOBJ.objectHelper().setObject("cleanBgColor", true)
                uICookToolOBJ.objectHelper().setObject("hidePureModeSwitchLayout", true)
                uICookToolOBJ.objectHelper().setObject("hideWarningLayout", true)
                uICookToolOBJ.objectHelper().setObject("hideWarningLayout", true)
                uICookToolOBJ.objectHelper().setObject("needIsolate", false)
                uICookToolOBJ.objectHelper().setObject("showRiskCheckbox", false)
                uICookToolOBJ.objectHelper().setObject("showSwlimitLearnMore", false)
                uICookToolOBJ.objectHelper().setObject("warningTitle", "彩蛋")
                uICookToolOBJ.objectHelper().setObject("warningUnknownText", "这是一条提醒")
                it.result = uICookToolOBJ
            }
    }
}