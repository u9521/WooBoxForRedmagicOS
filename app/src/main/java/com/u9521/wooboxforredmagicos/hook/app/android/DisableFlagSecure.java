/**
 * 代码来自 https://github.com/LSPosed/DisableFlagSecure
 * GPL-3.0 License
 **/
package com.u9521.wooboxforredmagicos.hook.app.android;

import static de.robv.android.xposed.XposedBridge.log;

import android.annotation.SuppressLint;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.view.SurfaceControl;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.u9521.wooboxforredmagicos.util.XSPUtils;
import com.u9521.wooboxforredmagicos.util.xposed.EasyXposedInit;
import com.u9521.wooboxforredmagicos.util.xposed.base.AppRegister;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

@SuppressLint({"PrivateApi", "BlockedPrivateApi"})
public class DisableFlagSecure extends EasyXposedInit implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam == null || !loadPackageParam.packageName.equals("android")) {
            return;
        }
        var classLoader = loadPackageParam.classLoader;

        try {
            deoptimizeSystemServer(classLoader);
        } catch (Throwable t) {
            log("deoptimize system server failed");
            log(t);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            // Screen record detection (V~Baklava)
            try {
                hookWindowManagerService(classLoader);
            } catch (Throwable t) {
                XposedBridge.log("hook WindowManagerService failed");
                XposedBridge.log(t);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            // Screenshot detection (U~Baklava)
            try {
                hookActivityTaskManagerService(classLoader);
            } catch (Throwable t) {
                log("hook ActivityTaskManagerService failed");
                log(t);
            }

            // Xiaomi HyperOS (U~Baklava)
            // OS2.0.250220.1.WOCCNXM.PRE
//            try {
//                hookHyperOS(classLoader);
//            } catch (ClassNotFoundException ignored) {
//            } catch (Throwable t) {
//                log("hook HyperOS failed", t);
//            }
        }

        // ScreenCapture in WindowManagerService (S~Baklava)
        try {
            hookScreenCapture(classLoader);
        } catch (Throwable t) {
            log("hook ScreenCapture failed");
            log(t);
        }

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
//            // Blackout permission check (S~T)
//            try {
//                hookActivityManagerService(classLoader);
//            } catch (Throwable t) {
//                log("hook ActivityManagerService failed", t);
//            }
//        }

        // WifiDisplay (S~Baklava) / OverlayDisplay (S~Baklava) / VirtualDisplay (U~Baklava)
        try {
            hookDisplayControl(classLoader);
        } catch (Throwable t) {
            log("hook DisplayControl failed");
            log(t);
        }

        // VirtualDisplay with MediaProjection (S~Baklava)
        try {
            hookVirtualDisplayAdapter(classLoader);
        } catch (Throwable t) {
            log("hook VirtualDisplayAdapter failed");
            log(t);
        }

        try {
            hookScreenshotHardwareBuffer(classLoader);
        } catch (Throwable t) {
            if (!(t instanceof ClassNotFoundException)) {
                log("hook ScreenshotHardwareBuffer failed");
                log(t);
            }
        }
        // OneUI
//        try {
//            hookOneUI(classLoader);
//        } catch (Throwable t) {
//            if (!(t instanceof ClassNotFoundException)) {
//                log("hook OneUI failed", t);
//            }
//        }

        // secureLocked flag
        try {
            // Screenshot
            hookWindowState(classLoader);
        } catch (Throwable t) {
            log("hook WindowState failed");
            log(t);
        }

        // oplus dumpsys
        // dumpsys window screenshot systemQuickTileScreenshotOut display_id=0
//        try {
//            hookOplus(classLoader);
//        } catch (Throwable t) {
//            if (!(t instanceof ClassNotFoundException)) {
//                log("hook Oplus failed", t);
//            }
//        }
    }


    private void deoptimizeSystemServer(ClassLoader classLoader) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException {

        deoptimizeMethod(
                classLoader.loadClass("com.android.server.wm.WindowStateAnimator"),
                "createSurfaceLocked");

        deoptimizeMethod(
                classLoader.loadClass("com.android.server.wm.WindowManagerService"),
                "relayoutWindow");

        for (int i = 0; i < 20; i++) {
            try {
                var clazz = classLoader.loadClass("com.android.server.wm.RootWindowContainer$$ExternalSyntheticLambda" + i);
                if (BiConsumer.class.isAssignableFrom(clazz)) {
                    deoptimizeMethod(clazz, "accept");
                }
            } catch (ClassNotFoundException ignored) {
            }
            try {
                var clazz = classLoader.loadClass("com.android.server.wm.DisplayContent$" + i);
                if (BiPredicate.class.isAssignableFrom(clazz)) {
                    deoptimizeMethod(clazz, "test");
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    private void hookWindowState(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        var windowStateClazz = classLoader.loadClass("com.android.server.wm.WindowState");
        var isSecureLockedMethod = windowStateClazz.getDeclaredMethod("isSecureLocked");
        XposedBridge.hookMethod(isSecureLockedMethod, SecureLockedHooker);
    }

    private static Field captureSecureLayersField;

    private void hookScreenCapture(ClassLoader classLoader) throws ClassNotFoundException, NoSuchFieldException {
        var screenCaptureClazz = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ?
                classLoader.loadClass("android.window.ScreenCapture") :
                SurfaceControl.class;
        var captureArgsClazz = classLoader.loadClass(Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ?
                "android.window.ScreenCapture$CaptureArgs" :
                "android.view.SurfaceControl$CaptureArgs");
        captureSecureLayersField = captureArgsClazz.getDeclaredField("mCaptureSecureLayers");
        captureSecureLayersField.setAccessible(true);
        hookMethods(screenCaptureClazz, ScreenCaptureHooker, "nativeCaptureDisplay", "nativeCaptureLayers");
    }

    private void hookDisplayControl(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        var displayControlClazz = Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ?
                classLoader.loadClass("com.android.server.display.DisplayControl") :
                SurfaceControl.class;
        var method = displayControlClazz.getDeclaredMethod(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM ?
                        "createVirtualDisplay" :
                        "createDisplay", String.class, boolean.class);
        XposedBridge.hookMethod(method, CreateDisplayHooker);
    }

    private void hookVirtualDisplayAdapter(ClassLoader classLoader) throws ClassNotFoundException {
        var displayControlClazz = classLoader.loadClass("com.android.server.display.VirtualDisplayAdapter");
        hookMethods(displayControlClazz, CreateVirtualDisplayLockedHooker, "createVirtualDisplayLocked");
    }

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    private void hookActivityTaskManagerService(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        var activityTaskManagerServiceClazz = classLoader.loadClass("com.android.server.wm.ActivityTaskManagerService");
        var iBinderClazz = classLoader.loadClass("android.os.IBinder");
        var iScreenCaptureObserverClazz = classLoader.loadClass("android.app.IScreenCaptureObserver");
        var method = activityTaskManagerServiceClazz.getDeclaredMethod("registerScreenCaptureObserver", iBinderClazz, iScreenCaptureObserverClazz);
        XposedBridge.hookMethod(method, ReturnNullHooker);
    }

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    private void hookWindowManagerService(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        var windowManagerServiceClazz = classLoader.loadClass("com.android.server.wm.WindowManagerService");
        var iScreenRecordingCallbackClazz = classLoader.loadClass("android.window.IScreenRecordingCallback");
        var method = windowManagerServiceClazz.getDeclaredMethod("registerScreenRecordingCallback", iScreenRecordingCallbackClazz);
        XposedBridge.hookMethod(method, ReturnFalseHooker);
    }

//    private void hookActivityManagerService(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
//        var activityTaskManagerServiceClazz = classLoader.loadClass("com.android.server.am.ActivityManagerService");
//        var method = activityTaskManagerServiceClazz.getDeclaredMethod("checkPermission", String.class, int.class, int.class);
//        XposedBridge.hookMethod(method, CheckPermissionHooker);
//    }

    private void hookScreenshotHardwareBuffer(ClassLoader classLoader) throws ClassNotFoundException, NoSuchMethodException {
        var screenshotHardwareBufferClazz = classLoader.loadClass(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE ?
                        "android.window.ScreenCapture$ScreenshotHardwareBuffer" :
                        "android.view.SurfaceControl$ScreenshotHardwareBuffer");
        var method = screenshotHardwareBufferClazz.getDeclaredMethod("containsSecureLayers");
        XposedBridge.hookMethod(method, ReturnFalseHooker);
    }

    @NonNull
    @Override
    public List<AppRegister> getRegisteredApp() {
        return Collections.emptyList();
    }

    XC_MethodHook CreateDisplayHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (enableHook()) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    var stackTrace = new Throwable().getStackTrace();
                    for (int i = 4; i < stackTrace.length && i < 8; i++) {
                        var name = stackTrace[i].getMethodName();
                        if (name.equals("createVirtualDisplayLocked")) {
                            return;
                        }
                    }
                }
                param.args[1] = true;
            }
        }
    };

//    XC_MethodHook CheckPermissionHooker = new XC_MethodHook() {
//        @Override
//        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//            var permission = param.args[0];
//            if ("android.permission.CAPTURE_BLACKOUT_CONTENT".equals(permission)) {
//                param.args[0] = "android.permission.READ_FRAME_BUFFER";
//            }
//        }
//    };

    XC_MethodHook ScreenCaptureHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            var captureArgs = param.args[0];
            if (enableHook()) {
                try {
                    captureSecureLayersField.set(captureArgs, true);
                } catch (IllegalAccessException t) {
                    XposedBridge.log("ScreenCaptureHooker failed");
                    XposedBridge.log(t);
                }
            }
        }
    };

    XC_MethodHook CreateVirtualDisplayLockedHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (enableHook()) {
                var caller = (int) param.args[2];
                if (caller >= 10000 && param.args[1] == null) {
                    // not os and not media projection
                    return;
                }
                for (int i = 3; i < param.args.length; i++) {
                    var arg = param.args[i];
                    if (arg instanceof Integer) {
                        var flags = (int) arg;
                        flags |= DisplayManager.VIRTUAL_DISPLAY_FLAG_SECURE;
                        param.args[i] = flags;
                        return;
                    }
                }
                XposedBridge.log("flag not found in CreateVirtualDisplayLockedHooker");
            }
        }
    };
    XC_MethodHook SecureLockedHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (enableHook()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    var walker = StackWalker.getInstance();
                    var match = walker.walk(frames -> frames
                            .map(StackWalker.StackFrame::getMethodName)
                            .limit(6)
                            .skip(2)
                            .anyMatch(s -> s.equals("setInitialSurfaceControlProperties") || s.equals("createSurfaceLocked")));
                    if (match) return;
                } else {
                    var stackTrace = new Throwable().getStackTrace();
                    for (int i = 4; i < stackTrace.length && i < 8; i++) {
                        var name = stackTrace[i].getMethodName();
                        if (name.equals("setInitialSurfaceControlProperties") ||
                                name.equals("createSurfaceLocked")) {
                            return;
                        }
                    }
                }
                param.setResult(false);
            }
        }
    };

    XC_MethodHook ReturnFalseHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (enableHook()) {
                param.setResult(false);
            }
        }
    };

    XC_MethodHook ReturnNullHooker = new XC_MethodHook() {
        @Override
        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
            if (enableHook()) {
                param.setResult(null);
            }
        }
    };

    private void hookMethods(Class<?> clazz, XC_MethodHook callback, String... names) {
        var list = Arrays.asList(names);
        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> list.contains(method.getName()))
                .forEach(method -> XposedBridge.hookMethod(method, callback));
    }

    private boolean enableHook() {
        return XSPUtils.INSTANCE.getBoolean("disable_flag_secure", false);
    }

}