package com.u9521.wooboxforredmagicos.hook.app.android

import com.github.kyuubiran.ezxhelper.ClassUtils
import com.github.kyuubiran.ezxhelper.finders.MethodFinder
import com.u9521.wooboxforredmagicos.util.XSPUtils
import com.u9521.wooboxforredmagicos.util.xposed.Deoptimizer
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge

class LogBlocker {
    private val deoptMap: HashMap<String, Array<String>> = HashMap()
    private val noServerLog = XSPUtils.getBoolean("block_system_server_log", false)
    private val nullHooker = object : XC_MethodHook() {
        override fun beforeHookedMethod(param: MethodHookParam?) {
            param!!.result = null
        }
    }

    fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        //APM_AudioPolicyManager
        val telecomLogClazz = ClassUtils.loadClass("android.telecom.Log")
        val telecomLogBlMethods = arrayOf("d", "e", "i", "v", "w", "wtf")
        val logClazz = ClassUtils.loadClass("android.util.Log")
        deoptMap["android.telecom.Logging.SessionManager"] =
            arrayOf("endParentSessions", "startSession", "cleanupStaleSessions")
        deoptMap["com.zte.security.ZTESecurityUtils"] = arrayOf("<all>")
        /*
        * Tag占比统计:
Tag                            出现次数       占比(%)
--------------------------------------------------
DmUtilRil                      1120       22.23
MindSyncLowRamMonitor          396        7.86
NetworkStats                   376        7.46
BroadcastQueue                 274        5.44
Scene-Network                  254        5.04
CpuFreezer...rServiceV2        243        4.82
Thanox-Core                    211        4.19
ContextImpl                    198        3.93
HsSecInterface                 196        3.89
ZTESecurityUtils               195        3.87
Scene-Audio                    183        3.63
Scene-Bluetooth                145        2.88
ZswNetworkAlignService         92         1.83
AlarmManager                   83         1.65
ZteBatteryService              65         1.29
PowerTracker                   52         1.03
NetworkPolicy                  49         0.97
DevicePolicyManager            46         0.91
WooBox                         40         0.79
LSPosed-Bridge                 40         0.79
NetworkManagement              30         0.60
WindowManager                  29         0.58
SensorService                  26         0.52
MindSync                       26         0.52
DisplayPowerState              25         0.50
Scene-AppInfo                  24         0.48
ActivityManager                21         0.42
AppWidgetServiceImpl           20         0.40
DisplayPow...roller2[0]        19         0.38
IntervalStats                  19         0.38
ZTEPermiss...ategyUtils        18         0.36
system_server                  16         0.32
SensorManager                  16         0.32
AutoLaunch...gerService        16         0.32
ZTEPrivacy...gerService        15         0.30
HS_FM_TAG                      15         0.30
BinderUnfreeze                 15         0.30
Scene-SysInfo                  14         0.28
UriGrantsManagerService        14         0.28
DisplayPow...troller[0]        14         0.28
ZteColorTe...Controller        14         0.28
SingleHandAdapter              12         0.24
DisplayManagerService          12         0.24
ColorfulLi...ateMachine        11         0.22
ConnectivityService            10         0.20
BATTERY_CHG                    9          0.18
AlarmManager_ZTE_ALIGN         8          0.16
ClipboardService               8          0.16
Sensor                         8          0.16
WifiServiceExterned            8          0.16
BroadcastQ...LaunchHook        8          0.16
GoogleOptimizer                8          0.16
Brightness...ngStrategy        8          0.16
PowerManagerService            7          0.14
AutomaticB...Controller        7          0.14
MindSyncService                6          0.12
SingleKeyGesture               6          0.12
RefreshRate                    6          0.12
LocalDisplayAdapter            6          0.12
DisplayDeviceRepository        6          0.12
LogicalDisplay                 6          0.12
Accessibil...sPopulator        6          0.12
InputDispatcher                6          0.12
ColorfulLightService           5          0.10
InputReader                    5          0.10
WifiClient...3:wlan0:Y]        5          0.10
AS.AudioService                5          0.10
ProcessStats                   4          0.08
WifiNetworkSelector            4          0.08
BrightnessSynchronizer         4          0.08
Compatibil...geReporter        3          0.06
WifiService                    3          0.06
InputManager-JNI               3          0.06
GameHighLightsCtrl             3          0.06
WifiConnectivityManager        3          0.06
SurfaceControl                 3          0.06
DreamManagerService            3          0.06
WifiDataStall                  3          0.06
BatteryService                 2          0.04
JobScheduler                   2          0.04
androidtc                      2          0.04
AIManagerService               2          0.04
PowerGroup                     2          0.04
ColorfulLightManager           2          0.04
PowerManag...mpWakeLock        2          0.04
DisplayBri...gySelector        2          0.04
ALSProbe                       2          0.04
UsbDeviceManager               2          0.04
DisplayDevice                  2          0.04
GameOperationDeviceCtrl        2          0.04
GameModeService                2          0.04
FastPairService                2          0.04
StorageManagerService          2          0.04
WifiLockManager                2          0.04
SideFpsEventHandler            2          0.04
ColorfulLightMissEvent         2          0.04
DreamController                2          0.04
CoreBackPreview                2          0.04
NHSS_StateManager              2          0.04
ZteBrightnessControl           2          0.04
GameMagicVoiceCtrl             2          0.04
ImeTracker                     2          0.04
NotificationService            2          0.04
TelephonyManager               1          0.02
libprocessgroup                1          0.02
ProcessStatsService            1          0.02
TrustManagerService            1          0.02
NearbyManager                  1          0.02
BrightnessTracker              1          0.02
WifiBroadcastQueue             1          0.02
MirrorDragCtrl                 1          0.02
PerfDataManager                1          0.02
Accessibil...gerService        1          0.02
NotificationHBUtils            1          0.02
NotificationHis tory            1          0.02
WificondScannerImpl            1          0.02
WifiNl80211Manager             1          0.02
WifiNative                     1          0.02
IE_Capabilities                1          0.02
        * */
        if (noServerLog) {
            doDeoptimize()
            MethodFinder.fromClass(telecomLogClazz).onEach {
                if (it.name in telecomLogBlMethods) {
                    XposedBridge.hookMethod(it, nullHooker)
                }
            }
//            MethodFinder.fromClass("com.zte.security.ZTESecurityUtils").onEach {
//                it.createHook {
//                    val hooker = null
//                    before {}
//                }
//            }
        }
    }

    private fun doDeoptimize() {
        for ((k, v) in deoptMap) {
            val clazz = ClassUtils.loadClass(k)
            if (v[0] == "<all>") {
                Deoptimizer.deoptimizeAllMethods(clazz)
                continue
            }
            Deoptimizer.deoptimizeMethods(clazz, *v)
        }
    }
}
