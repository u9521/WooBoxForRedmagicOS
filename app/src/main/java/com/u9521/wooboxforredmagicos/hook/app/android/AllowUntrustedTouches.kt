package com.u9521.wooboxforredmagicos.hook.app.android

import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object AllowUntrustedTouches : HookRegister() {
    override fun init() = hasEnable("allow_untrusted_touches") {
        //Landroid/view/InputWindowHandle;-><init>(Landroid/view/InputWindowHandle;)V
        // .field public static final blacklist ALLOW:I = 0x2
        // .field public static final blacklist BLOCK_UNTRUSTED:I = 0x0
        // .field public static final blacklist USE_OPACITY:I = 0x1
        MethodFinder.fromClass("com.android.server.wm.WindowState")
            .filterByName("getTouchOcclusionMode")
            .first().createBeforeHook {
                it.result = 2
            }
    }
}