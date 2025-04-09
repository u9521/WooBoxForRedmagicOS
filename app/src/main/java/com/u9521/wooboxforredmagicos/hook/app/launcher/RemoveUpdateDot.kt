package com.u9521.wooboxforredmagicos.hook.app.launcher

import android.widget.TextView
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import java.lang.reflect.Field

object RemoveUpdateDot : HookRegister() {
    override fun init() = hasEnable("launcher_remove_update_dot") {
        return@hasEnable
//        findMethod("com.android.launcher3.OplusBubbleTextView") {
//
//            name == "applyLabel" && parameterCount == 3
//        }.hookBefore {
//            val field: Field =
//                loadClass("com.android.launcher3.model.data.ItemInfo").getDeclaredField("title")
//            field.isAccessible = true
//            val title = field[it.args.get(0)] as CharSequence
//            (it.thisObject as TextView).text = title
//            it.result = null
//        }
    }
}