package tornaco.apps.shortx.core.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Process;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.util.List;

@SuppressWarnings("deprecation")
public class PkgUtils {
    private PkgUtils() {
    }

    public static CharSequence loadNameByPkgName(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();

        try {
            ApplicationInfo info = pm.getApplicationInfo(pkg, 8192);
            return info == null ? null : info.loadLabel(pm);
        } catch (PackageManager.NameNotFoundException var4) {
            return null;
        }
    }

    public static boolean isPkgInstalled(Context context, String pkg) {
        return isPkgInstalled(context, pkg, UserHandle.getCallingUserId());
    }

    public static boolean isPkgInstalled(Context context, String pkg, int userId) {
        return getApplicationInfoAsUser(context, pkg, userId) != null;
    }

    public static ApplicationInfo getApplicationInfoAsUser(Context context, String pkg, int userId) {
        if (!OsUtils.isOOrAbove()) {
            return getApplicationInfo(context, pkg);
        }
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getApplicationInfoAsUser(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_META_DATA, userId);
        } catch (Throwable e) {
            return null;
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo info;
            info = pm.getApplicationInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_META_DATA);
            return info;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        } catch (Throwable e) {
            return null;
        }
    }

    public static PackageInfo getPackageInfo(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info;
            info = pm.getPackageInfo(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES);
            return info;
        } catch (Throwable e) {
            return null;
        }
    }

    @SuppressLint("MissingPermission")
    public static PackageInfo getPackageInfoAsUser(Context context, String pkg, int userId) {
        if (!OsUtils.isOOrAbove()) {
            return getPackageInfo(context, pkg);
        }
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo info;
            info = pm.getPackageInfoAsUser(pkg, PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_META_DATA, userId);
            return info;
        } catch (Throwable e) {
            return null;
        }
    }

    public static List<ApplicationInfo> getInstalledApplications(Context context) {
        if (OsUtils.isNOrAbove()) {
            return getInstalledApplications(context, PackageManager.MATCH_UNINSTALLED_PACKAGES | PackageManager.GET_META_DATA);
        } else {
            return getInstalledApplications(context, PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_META_DATA);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public static List<ApplicationInfo> getInstalledApplications(Context context, int flags) {
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplications(flags);
    }

    public static List<ApplicationInfo> getInstalledApplicationsAsUser(Context context, int userId) {
        int flags = PackageManager.GET_META_DATA;
        if (!OsUtils.isOOrAbove()) {
            return getInstalledApplications(context, flags);
        }
        return getInstalledApplicationsAsUser(context, userId, flags);
    }

    public static List<ApplicationInfo> getInstalledApplicationsAsUser(Context context, int userId, int flags) {
        if (!OsUtils.isOOrAbove()) {
            return getInstalledApplications(context, flags);
        }
        PackageManager pm = context.getPackageManager();
        return pm.getInstalledApplicationsAsUser(flags, userId);
    }

    public static String getPathForPackage(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        int flags = OsUtils.isNOrAbove() ? PackageManager.MATCH_UNINSTALLED_PACKAGES : PackageManager.GET_UNINSTALLED_PACKAGES;
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(pkg, flags);
            return applicationInfo.publicSourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static boolean isHomeIntent(Intent intent) {
        return intent != null && intent.hasCategory(Intent.CATEGORY_HOME);
    }

    public static boolean isMainIntent(Intent intent) {
        return intent != null
                && Intent.ACTION_MAIN.equals(intent.getAction())
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER);
    }

    public static String packageNameOf(Intent intent) {
        if (intent == null) {
            return null;
        }
        String packageName = intent.getPackage();
        if (packageName != null) {
            return packageName;
        }
        if (intent.getComponent() == null) {
            return null;
        }
        return intent.getComponent().getPackageName();
    }

    public static boolean isInputMethodApp(Context context, String pkgName) {

        PackageManager pm = context.getPackageManager();
        boolean isIme = false;
        PackageInfo pkgInfo;
        try {
            pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_SERVICES);
            if (pkgInfo != null) {
                ServiceInfo[] servicesInfos = pkgInfo.services;
                if (null != servicesInfos) {
                    for (ServiceInfo sInfo : servicesInfos) {
                        if (null != sInfo.permission && sInfo.permission.equals(Manifest.permission.BIND_INPUT_METHOD)) {
                            isIme = true;
                            break;
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return isIme;
    }

    // Fix dead lock issue when call this along with xxx framework patch.
    public static boolean isDefaultSmsApp(Context context, String packageName) {
        // String def = Telephony.Sms.getDefaultSmsPackage(context);
        // return def != null && def.equals(packageName);
        return false;
    }

    public static boolean isAndroid(String packageName) {
        // String def = Telephony.Sms.getDefaultSmsPackage(context);
        // return def != null && def.equals(packageName);
        return "android".equals(packageName);
    }

    // Check if uid is system, shell or phone.
    public static boolean isSystemOrPhoneOrShell(int uid) {
        return uid <= 2000
                || (uid > UserHandle.PER_USER_RANGE && (uid % UserHandle.PER_USER_RANGE <= 2000));
    }

    public static boolean isSystemCall(long uid) {
        return uid == 1000
                || (uid > UserHandle.PER_USER_RANGE && (uid % UserHandle.PER_USER_RANGE == 1000));
    }

    public static boolean isShell(int uid) {
        return uid == 2000;
    }

    public static String resolvePackageName(int uid, String packageName) {
        if (uid == Process.ROOT_UID) {
            return "root";
        } else if (uid == Process.SHELL_UID) {
            return "com.android.shell";
        } else if (uid == Process.MEDIA_UID) {
            return "media";
        } else if (uid == Process.AUDIOSERVER_UID) {
            return "audioserver";
        } else if (uid == Process.CAMERASERVER_UID) {
            return "cameraserver";
        } else if (uid == Process.SYSTEM_UID && packageName == null) {
            return "android";
        }
        return packageName;
    }

    public static int resolveUid(String packageName) {
        if (packageName == null) {
            return -1;
        }
        switch (packageName) {
            case "root":
                return Process.ROOT_UID;
            case "shell":
                return Process.SHELL_UID;
            case "media":
                return Process.MEDIA_UID;
            case "audioserver":
                return Process.AUDIOSERVER_UID;
            case "cameraserver":
                return Process.CAMERASERVER_UID;
        }
        return -1;
    }

    private static PackageInfo getPkgInfo(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    public static ComponentName getComponentName(ActivityInfo activityInfo) {
        return new ComponentName(activityInfo.packageName, activityInfo.name);
    }

    public static ComponentName getComponentName(ServiceInfo serviceInfo) {
        return new ComponentName(serviceInfo.packageName, serviceInfo.name);
    }

    public static ComponentName getComponentName(ProviderInfo providerInfo) {
        return new ComponentName(providerInfo.packageName, providerInfo.name);
    }

    public static PackageInfo getPackageInfo(Context context, String path, boolean verifySign) {
        int flags = PackageManager.GET_ACTIVITIES | PackageManager.GET_SERVICES
                | PackageManager.GET_PROVIDERS | PackageManager.GET_RECEIVERS
                | PackageManager.GET_META_DATA;
        if (verifySign) {
            flags |= PackageManager.GET_SIGNATURES;
        }
        PackageManager pm = context.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(path, flags);
        if (packageInfo == null || packageInfo.applicationInfo == null) {
            return null;
        }
        packageInfo.applicationInfo.sourceDir = path;
        packageInfo.applicationInfo.publicSourceDir = path;
        return packageInfo;
    }

    public static InputMethodInfo getDefaultInputMethodInfo(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();
            final int N = mInputMethodProperties.size();
            for (int i = 0; i < N; i++) {
                InputMethodInfo imi = mInputMethodProperties.get(i);
                if (imi.getId().equals(Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.DEFAULT_INPUT_METHOD))) {
                    //imi contains the information about the keyboard you are using
                    return imi;
                }
            }
        } catch (Throwable e) {
        }
        return null;
    }

    public static String getApkPath(Context context, String pkgName) {
        ApplicationInfo applicationInfo = getApplicationInfo(context, pkgName);
        if (applicationInfo == null) return null;
        return applicationInfo.publicSourceDir;
    }

    /**
     * Directory where installed applications are stored
     */
    private static final File APP_INSTALL_DIR = new File(Environment.getDataDirectory(), "app");

    public static File getApkPath(String pkgName) {
        final File[] files = APP_INSTALL_DIR.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        for (File file : files) {
            final boolean isPackage =
                    (isApkFile(file) || file.isDirectory()) && !isStageName(file.getName());
            if (!isPackage) {
                // Ignore entries which are not packages
                continue;
            }
            PackageParser.PackageLite lite = parseApk(file);
            if (lite != null && pkgName.equals(lite.packageName)) {
                // Find apk.
                return findFirstApkFile(new File(lite.codePath));
            }
        }
        return null;
    }

    private static File findFirstApkFile(File file) {
        if (isApkFile(file)) return file;
        if (!file.isDirectory()) return null;
        final File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }
        for (File f : files) {
            if (isApkFile(f)) return f;
        }
        return null;
    }

    // PackageInstallerService
    private static boolean isStageName(String name) {
        boolean isFile = name.startsWith("vmdl") && name.endsWith(".tmp");
        boolean isContainer = name.startsWith("smdl") && name.endsWith(".tmp");
        boolean isLegacyContainer = name.startsWith("smdl2tmp");
        return isFile || isContainer || isLegacyContainer;
    }

    private static boolean isApkFile(File file) {
        return PackageParser.isApkFile(file);
    }

    private static PackageParser.PackageLite parseApk(File file) {
        try {
            return PackageParser.parsePackageLite(file, 0 /* No flags */);
        } catch (PackageParser.PackageParserException e) {
            Log.e("parseApk got error: %s", Log.getStackTraceString(e));
        }
        return null;
    }

    public static void launchPkgSettings(Context context, String pkgName) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", pkgName, null);
        intent.setData(uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
