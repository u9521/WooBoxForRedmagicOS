package com.u9521.wooboxforredmagicos.hook.app

import com.u9521.wooboxforredmagicos.hook.app.mtpFileBrowser.HideMTPCategoryBrowse
import com.u9521.wooboxforredmagicos.hook.app.mtpFileBrowser.RenameRootName
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister
import de.robv.android.xposed.callbacks.XC_LoadPackage

object MtpFileBrowser : AppRegister() {
    override val packageName: List<String> = listOf("cn.nubia.filebrowser")
    override val processName: List<String> = emptyList()
    override val logTag: String = "WooBox-MtpFileBrowser"
    override val loadDexkit: Boolean = false
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        autoInitHooks(
            lpparam,
            HideMTPCategoryBrowse,//隐藏MTP分类浏览
            RenameRootName,//重命名路径浏览名称
        )
    }
}