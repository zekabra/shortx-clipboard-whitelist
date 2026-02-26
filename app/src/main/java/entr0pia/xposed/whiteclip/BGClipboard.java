package entr0pia.xposed.whiteclip;

import static de.robv.android.xposed.XposedHelpers.*;

import android.os.Build;
import android.util.Log;

import java.lang.String;

import de.robv.android.xposed.*;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class BGClipboard implements IXposedHookLoadPackage {
    static String logtag = "=== ClipboardService Hook ===";
    static String whiteList = "tornaco.apps.shortx";

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equals("android")) {
            return;
        }
        XposedBridge.log(BGClipboard.logtag + " - Loaded app: " + lpparam.packageName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            Log.d(BGClipboard.logtag, "hook method for SDK Version:  " + Build.VERSION_CODES.UPSIDE_DOWN_CAKE);
            hook34(lpparam);
        } else {
            Log.d(BGClipboard.logtag, "hook method for SDK Version:  " + Build.VERSION_CODES.Q);
            hook(lpparam);
        }
    }

    private void hook34(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> clazz = findClass("com.android.server.clipboard.ClipboardService", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz,
                "clipboardAccessAllowed",
                int.class,
                String.class,
                String.class,
                int.class,
                int.class,
                int.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (whiteList.equals(param.args[1])) {
                            param.setResult(true);
                        }
                    }
                });
    }

    private void hook(XC_LoadPackage.LoadPackageParam lpparam) {
        Class<?> clazz = findClass("com.android.server.clipboard.ClipboardService", lpparam.classLoader);
        XposedHelpers.findAndHookMethod(clazz,
                "clipboardAccessAllowed",
                int.class,
                String.class,
                String.class,
                int.class,
                int.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        if (whiteList.equals(param.args[1])) {
                            param.setResult(true);
                        }
                    }
                });
    }
}
