package com.u9521.wooboxforredmagicos.hook.app.settings

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.text.TextUtils
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object UsbInstallNoVerify : HookRegister() {
    private val adbInstallSet = "adb_install_enabled"
    private fun setAdbInstall(cr: ContentResolver, int: Int) {
        val putIntMe =
            getDefaultCL().loadClass("android.provider.Settings\$System").getDeclaredMethod(
                "putInt",
                ContentResolver::class.java,
                String::class.java,
                Int::class.javaPrimitiveType
            )
        putIntMe.invoke(null, cr, adbInstallSet, int)
        Log.i("set adbInstall: ContentResolver:$cr settingString:$adbInstallSet int:$int")
    }

    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("usb_install_switch_skip_verify") {
        val adbIPrefClazz =
            getDefaultCL().loadClass("com.zte.settings.development.EnableAdbInstallPreferenceController")
        val aPrefClazz =
            getDefaultCL().loadClass("com.android.settingslib.core.AbstractPreferenceController")
        val preferClazz = getDefaultCL().loadClass("androidx.preference.Preference")
        val twoStatePrefer = getDefaultCL().loadClass("androidx.preference.TwoStatePreference")
        val prefTMe = adbIPrefClazz.getDeclaredMethod("handlePreferenceTreeClick", preferClazz)
        val prefTreeHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                val adbIPrefsKey = adbIPrefClazz.getDeclaredMethod("getPreferenceKey")
                    .invoke(param!!.thisObject) as String
                val clickedKey =
                    preferClazz.getDeclaredMethod("getKey").invoke(param.args[0]) as String
                if (!TextUtils.equals(clickedKey, adbIPrefsKey)) {
                    // 点的不是USB安装
                    param.result = false
                    return
                }
                // switchPreference
                val adbIswP = adbIPrefClazz.getDeclaredField("mEnableAdbInstall")
                    .apply { isAccessible = true }.get(param.thisObject)
                val contentResolver =
                    (aPrefClazz.getDeclaredField("mContext").apply { isAccessible = true }
                        .get(param.thisObject) as Context).contentResolver

                if (twoStatePrefer.getDeclaredMethod("isChecked").invoke(adbIswP) as Boolean) {
                    // 打开ADB安装
                    setAdbInstall(contentResolver, 1)
                    param.result = true
                    return
                } else {
                    setAdbInstall(contentResolver, 0)
                    twoStatePrefer.getDeclaredMethod("setChecked", Boolean::class.javaPrimitiveType)
                        .invoke(adbIswP, false)
                    param.result = true
                    return
                }
            }
        }
        XposedBridge.hookMethod(prefTMe, prefTreeHooker)
    }
}