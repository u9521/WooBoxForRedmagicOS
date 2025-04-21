package com.u9521.wooboxforredmagicos.fragment

import cn.fkj233.ui.activity.data.InitView.ItemData
import com.u9521.wooboxforredmagicos.activity.SettingsActivity

abstract class MyFragment {
    abstract val regKey: String
    protected abstract val iData: ItemData.() -> Unit
    protected var mactivity: SettingsActivity? = null
    fun registerView(activity: SettingsActivity, title: String, hideMenu: Boolean) {
        mactivity = activity
        activity.mInitvew!!.register(regKey, title, hideMenu, iData)
    }
}