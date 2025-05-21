package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object RmWindowReplyLimits : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() {
        hasEnable("rm_window_reply_restriction") {
            //一些hook点记录
//            val activityRecordClazz = ClassUtils.loadClass("com.android.server.wm.ActivityRecord")
//            val WmTaskClazz = ClassUtils.loadClass("com.android.server.wm.Task")
//            //show unsupported info
//            MethodFinder.fromClass("com.android.server.wm.TaskDisplayAreaMifavor")
//                .filterByName("isSupportWindowReply")
//                .filterByParamTypes(Int::class.javaPrimitiveType).first().createBeforeHook {
//                    it.result = true
//                }
//            //check is unsupported
//            MethodFinder.fromClass("com.android.server.wm.TaskDisplayAreaMifavor")
//                .filterByName("isSupportWindowReplyState")
//                .filterByParamTypes(activityRecordClazz, Context::class.java).first()
//                .createBeforeHook {
//                    it.result = 0
//                }
//            //check is unsupported
//            MethodFinder.fromClass("com.android.server.wm.TaskDisplayAreaMifavor")
//                .filterByName("isSupportWindowReplyStateForTask")
//                .filterByParamTypes(WmTaskClazz, Context::class.java).first()
//                .createAfterHook {
//                    it.result = 0
//                }

            //allow all package
            val pkgInWhiteList = getDefaultCL().loadClass("android.app.WindowReplyUtils")
                .getDeclaredMethod("isForceSupportWhiteListForWR", String::class.java)
            val pkgWhiteListHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = true
                }
            }
            XposedBridge.hookMethod(pkgInWhiteList, pkgWhiteListHooker)
        }
        hasEnable("rm_window_reply_count_restriction") {
            /*
           notice:
           调整小窗大小的方法在下面，没有对小窗数量超过三个的情况进行处理，所以只能有三个小窗进入挂起（缩成一个图标），
           除了重写，没啥好办法
           Lcom/android/server/wm/TaskMifavor;->resizeForWR(Lcom/android/server/wm/Task;Lcom/android/server/wm/DisplayContent;IIILandroid/content/Context;Landroid/graphics/Rect;ZZ)V
           */
            //patch max window count
            val isMaxForMulti =
                getDefaultCL().loadClass("com.android.server.wm.ActivityTaskManagerService")
                    .getDeclaredMethod("isReachWrMaxSizeForMulti")
            val maxMultiHooker = object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam?) {
                    super.beforeHookedMethod(param)
                    param!!.result = false
                }
            }
            XposedBridge.hookMethod(isMaxForMulti, maxMultiHooker)

//            val wmTaskClazz = ClassUtils.loadClass("com.android.server.wm.Task")
//            val wmDisplayContentClazz = ClassUtils.loadClass("com.android.server.wm.DisplayContent")
//            val rectClazz = ClassUtils.loadClass("android.graphics.Rect")

//            MethodFinder.fromClass("com.android.server.wm.TaskMifavor").filterByName("resizeForWR")
//                .first().createBeforeHook{
//                    //TODO:rewrite the method
//                }
        }
    }
}