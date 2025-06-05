package com.u9521.wooboxforredmagicos.hook.app.android

import android.annotation.SuppressLint
import android.content.Context
import com.u9521.wooboxforredmagicos.util.hasEnable
import com.u9521.wooboxforredmagicos.util.xposed.base.HookRegister
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

object BlockTelemetryService : HookRegister() {
    @SuppressLint("PrivateApi")
    override fun init() = hasEnable("block_telemetry_service") {

        val gameZteTMClazz =
            getDefaultCL().loadClass("com.android.server.redmagic.trackclient.ZteTrackManager")
        val zteTMClazz =
            getDefaultCL().loadClass("com.android.server.datacollection.ZteTrackManager")

        val gameInitMe = gameZteTMClazz.getDeclaredMethod("init", Context::class.java)
        val initMe = zteTMClazz.getDeclaredMethod("init", Context::class.java)
        val initHooker = object : XC_MethodHook() {
            override fun beforeHookedMethod(param: MethodHookParam?) {
                super.beforeHookedMethod(param)
                param!!.result = null
            }
        }
        XposedBridge.hookMethod(gameInitMe, initHooker)
        XposedBridge.hookMethod(initMe, initHooker)
    }
}