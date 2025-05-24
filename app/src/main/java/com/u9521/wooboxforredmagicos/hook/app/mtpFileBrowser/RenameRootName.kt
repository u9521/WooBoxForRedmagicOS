package com.u9521.wooboxforredmagicos.hook.app.mtpFileBrowser

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RenameRootName : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        val nubiaMtpStorage =
            getDefaultCL().loadClass("cn.nubia.filebrowser.mtpserver.mtp.MtpStorage")
        val mDescription = nubiaMtpStorage.getDeclaredField("mDescription").apply { isAccessible=true }
        val mStorageId = nubiaMtpStorage.getDeclaredField("mStorageId").apply { isAccessible=true }
        val addStorage =
            getDefaultCL().loadClass("cn.nubia.filebrowser.mtpserver.mtp.MtpServer")
                .getDeclaredMethod("addStorage", nubiaMtpStorage)
        val addStorageHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                if (!XSPUtils.getBoolean("rename_mtp_storage_root_name_sw", false)) {
                    return
                }
                val volume = nubiaMtpStorage.cast(param!!.args[0])
//                Log.e("mStorageId is:${mStorageId.getInt(volume)}")
                if (mStorageId.getInt(volume) > 65537) {
                    return
                }
                val description = XSPUtils.getString("rename_mtp_storage_root_name", "内部存储设备")
                mDescription.set(volume, description)
//                Log.e("root object is:${Gson().toJson(volume)}")
//                param!!.result = null
            }
        }
        XposedBridge.hookMethod(addStorage, addStorageHooker)
    }
}