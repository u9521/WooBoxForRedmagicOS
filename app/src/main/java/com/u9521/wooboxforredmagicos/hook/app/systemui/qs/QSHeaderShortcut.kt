package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.RelativeLayout
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.Log
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge


object QSHeaderShortcut : HookRegister() {
    override fun init() {
        val ccHeaderClazz = getDefaultCL().loadClass("com.zte.controlcenter.widget.CCHeaderView")
        val startActivityMe =
            ccHeaderClazz.getDeclaredMethod(
                "postStartActivityDismissingKeyguard",
                Intent::class.java
            )

        // they not bind clock yet
//        hasEnable("qs_header_shortcut_redir_clock") {
//            MethodFinder.fromClass(CCHVClazz)
//                .filterByName("handleClickClock").first().createBeforeHook {
//                    SAMethod.invoke(it.thisObject, Intent("android.intent.action.SHOW_ALARMS"));
//                    it.result = null
//                }
//        }
        hasEnable("qs_header_shortcut_redirect_calendar") {
            val clickDateMe = ccHeaderClazz.getDeclaredMethod("handleClickDate")
            val clickDateHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    startActivityMe.invoke(
                        param!!.thisObject,
                        Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time"))
                    )
                    param.result = null
                }
            }
            XposedBridge.hookMethod(clickDateMe, clickDateHooker)
        }
        hasEnable("qs_header_shortcut_redirect_search") {
            val clickSearchMe = ccHeaderClazz.getDeclaredMethod("handleClickSearch")
            val clickSearchHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
                    val resolveInfo: ResolveInfo? =
                        (param!!.thisObject as RelativeLayout).context.packageManager.resolveActivity(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    if (resolveInfo != null) {
                        val packageName = resolveInfo.activityInfo.packageName
                        val className = resolveInfo.activityInfo.name
                        startActivityMe.invoke(
                            param.thisObject,
                            Intent().setClassName(packageName, className)
                        )
                    } else {
                        Log.ex("No default browser was found!!", logInRelease = true)
                    }
                    param.result = null
                }
            }
            XposedBridge.hookMethod(clickSearchMe, clickSearchHooker)
        }
    }
}