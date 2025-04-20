package com.u9521.wooboxforredmagicos.hook.app.systemui.qs

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.RelativeLayout
import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.HookFactory.`-Static`.createBeforeHook
import com.github.kyuubiran.ezxhelper.Log
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister


object QSHeaderShortcut : HookRegister() {
    override fun init() {
        val CCHVClazz = ClassUtils.loadClass("com.zte.controlcenter.widget.CCHeaderView")
        val SAMethod =
            MethodFinder.fromClass(CCHVClazz).filterByName("postStartActivityDismissingKeyguard")
                .first()
        // they not bind clock yet
//        hasEnable("qs_header_shortcut_redir_clock") {
//            MethodFinder.fromClass(CCHVClazz)
//                .filterByName("handleClickClock").first().createBeforeHook {
//                    SAMethod.invoke(it.thisObject, Intent("android.intent.action.SHOW_ALARMS"));
//                    it.result = null
//                }
//        }
        hasEnable("qs_header_shortcut_redir_calendar") {
            MethodFinder.fromClass(CCHVClazz)
                .filterByName("handleClickDate").first().createBeforeHook {
                    SAMethod.invoke(
                        it.thisObject,
                        Intent(Intent.ACTION_VIEW, Uri.parse("content://com.android.calendar/time"))
                    )
                    it.result = null
                }
        }
        hasEnable("qs_header_shortcut_redir_search") {
            MethodFinder.fromClass(CCHVClazz)
                .filterByName("handleClickSearch").first().createBeforeHook {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
                    val resolveInfo: ResolveInfo? =
                        (it.thisObject as RelativeLayout).context.packageManager.resolveActivity(
                            intent,
                            PackageManager.MATCH_DEFAULT_ONLY
                        )
                    if (resolveInfo != null) {
                        val packageName = resolveInfo.activityInfo.packageName
                        val className = resolveInfo.activityInfo.name
                        SAMethod.invoke(
                            it.thisObject,
                            Intent().setClassName(packageName, className)
                        )
                    } else {
                        Log.ex("DefaultBrowser No default browser found")
                    }
                    it.result = null
                }
        }
    }
}