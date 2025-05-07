package com.u9521.wooboxforredmagicos.hook.app.nfcService

import android.content.Intent
import android.os.UserHandle
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister

object ConfirmLaunchAPP : HookRegister() {
    private const val TARGET_CLASS = "com.android.nfc.NfcDispatcher\$DispatchInfo"
    private const val TARGET_METHOD = "tryStartActivity"

    // 存储拦截到的启动参数
    private var interceptedRootIntent: Intent? = null
    private var interceptedUserHandle: UserHandle? = null
    private var interceptedThis: Any? = null

    override fun init() = hasEnable("nfc_confirm_launch_app") {
        // TODO: wait to implement

    }


//    fun beforeHookedMethod(param: MethodHookParam) {
//        interceptedThis = param.thisObject
//        // 拦截 context.startActivityAsUser 调用
//        XposedHelpers.findAndHookMethod(
//            Context::class.java,
//            "startActivityAsUser",
//            Intent::class.java,
//            UserHandle::class.java,
//            object : XC_MethodHook() {
//                override fun beforeHookedMethod(innerParam: MethodHookParam) {
//                    // 只拦截 NfcDispatcher 内部的调用
//                    if (innerParam.thisObject == XposedHelpers.getObjectField(
//                            interceptedThis,
//                            "context"
//                        )
//                    ) {
//                        interceptedRootIntent = innerParam.args[0] as Intent
//                        interceptedUserHandle = innerParam.args[1] as UserHandle
//
//                        // 拦截原始调用
//                        innerParam.result = null
//                        Log.d("NfcHook", "Intercepted activity launch")
//                    }
//                }
//            })
//    }
//
//    fun afterHookedMethod(param: MethodHookParam) {
//        // 确保有拦截到启动请求
//        if (interceptedRootIntent == null) return
//
//        val context = XposedHelpers.getObjectField(
//            param.thisObject,
//            "context"
//        ) as? Context ?: return
//
//        val launchIntent = interceptedRootIntent?.getParcelableExtra<Intent>(
//            "launchIntent"
//        ) ?: return
//
//        // 获取目标应用信息
//        val pkgName = launchIntent.component?.packageName
//            ?: launchIntent.`package`
//            ?: return
//
//        try {
//            // 在 UI 线程显示确认对话框
//            Handler(Looper.getMainLooper()).post {
//                val pm = context.packageManager
//                try {
//                    val appInfo = pm.getApplicationInfo(pkgName, 0)
//
//                    AlertDialog.Builder(context)
//                        .setTitle("NFC 启动确认")
//                        .setMessage("允许通过 NFC 启动 ${appInfo.loadLabel(pm)}?")
//                        .setPositiveButton("允许") { _, _ ->
//                            try {
//                                // 重新执行原始调用
//                                val targetContext = XposedHelpers.getObjectField(
//                                    interceptedThis,
//                                    "context"
//                                ) as Context
//                                targetContext.startActivityAsUser(
//                                    interceptedRootIntent,
//                                    interceptedUserHandle
//                                )
//                                Toast.makeText(
//                                    context,
//                                    "已允许启动",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            } catch (e: Throwable) {
//                                XposedBridge.log(e)
//                            }
//                        }
//                        .setNegativeButton("拒绝") { _, _ ->
//                            Toast.makeText(
//                                context,
//                                "已阻止启动",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                        .setCancelable(false)
//                        .show()
//                } catch (e: PackageManager.NameNotFoundException) {
//                    XposedBridge.log("Package not found: $pkgName")
//                }
//            }
//        } catch (t: Throwable) {
//            XposedBridge.log(t)
//        }
//        // 清理拦截状态
//        interceptedRootIntent = null
//        interceptedUserHandle = null
//    }
}