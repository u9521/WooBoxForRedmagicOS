package com.u9521.wooboxforredmagicos.hook.app.settings

import android.text.TextUtils
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.ObjectHelper.Companion.objectHelper
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object UsbInstallNoVerify : HookRegister() {
    private val adbinstallPCclazz =
        ClassUtils.loadClass("com.zte.settings.development.EnableAdbInstallPreferenceController")
    private val APCclzz =
        ClassUtils.loadClass("com.android.settingslib.core.AbstractPreferenceController")
    private val ADBinstallSet = "adb_install_enabled"
    private fun systemPutInt(contentResolver: Any, string: String, int: Int) {
        Log.i("$contentResolver $string $int")
        val settingSysclazz = ClassUtils.loadClass("android.provider.Settings\$System")
        MethodFinder.fromClass(settingSysclazz).filterByName("putInt").filterByParamCount(3)
            .first().invoke(null, contentResolver, string, int)
    }

    override fun init() = hasEnable("usb_install_switch_skip_verify") {
        Log.i("hook start")
        MethodFinder.fromClass(adbinstallPCclazz).filterByName("handlePreferenceTreeClick")
            .filterByReturnType(Boolean::class.java).first()
            .createBeforeHook {
                val adbinstallkey =
                    it.thisObject.objectHelper().invokeMethodBestMatch("getPreferenceKey")
                val preKey = it.args[0].objectHelper().invokeMethodBestMatch("getKey") as String
                if (!TextUtils.equals(
                        preKey,
                        adbinstallkey.toString()
                    )
                ) {
                    it.result = false
                    return@createBeforeHook
                }
                val adbiSP = it.thisObject.objectHelper().getObjectOrNull("mEnableAdbInstall")
                val adbiCR = APCclzz.getDeclaredField("mContext").apply { isAccessible = true }
                    .get(it.thisObject)!!
                    .objectHelper()
                    .invokeMethodBestMatch("getContentResolver")!!
                if (adbiSP!!.objectHelper().invokeMethodBestMatch("isChecked") as Boolean) {
                    systemPutInt(adbiCR, ADBinstallSet, 1)
                    it.result = true
                    return@createBeforeHook
                } else {
                    adbiSP.objectHelper()
                        .invokeMethodBestMatch("setChecked", params = arrayOf(false))
                    systemPutInt(adbiCR, ADBinstallSet, 0)
                    it.result = true
                    return@createBeforeHook
                }
            }
    }
}