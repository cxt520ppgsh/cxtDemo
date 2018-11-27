package com.flyaudio.base.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import com.flyaudio.base.BuildConfig;
import com.flyaudio.base.R;
import com.flyaudio.base.application.BaseApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LKUtil {
    private static final String TAG = LKUtil.class.getSimpleName();
    private static final int MILL_ONE_DAY = 24 * 60 * 60 * 1000;
    public static final DecimalFormat DECIMAL_ONE_POINT = new DecimalFormat("0.0");

    public static Uri decodeUri(Uri uri) {
        if (uri != null) {
            uri = Uri.parse(Uri.decode(uri.toString()));
        }
        return uri;
    }

    //http:// or https:// or youxuan://
    public static Uri parseUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        if (url.startsWith("http")) {
            String encodeUrl = Uri.encode(url);
            return new Uri.Builder()
                    .scheme(BuildConfig.APP_SCHEME)
                    .authority("web")
                    .appendQueryParameter("url", encodeUrl)
                    .build();
        }
        return Uri.parse(url);
    }


    /**
     * 在app内部打开url
     * 如： youxuan://commodity  http://www.lukou.com
     *
     * @param url
     */
    public static boolean startUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, parseUrl(url));
        if (hasAppsStartIntent(context, intent)) {
            context.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        if (context == null) {
            return (int) dipValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将dp值转换为px值
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        if (context == null) {
            return (int) pxValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * @param context
     * @param px
     * @return
     */
    public static float px2sp(Context context, Float px) {
        if (context == null) {
            return px.intValue();
        }
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    /**
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        if (context == null) {
            return sp;
        }
        Resources r = context.getResources();
        float size = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                r.getDisplayMetrics());
        return size;
    }

    public static String parseHost(Intent intent) {
        return (intent == null ? null : intent.getData()) == null ? "" : intent
                .getData().getHost();
    }

    public static boolean isWIFIConnected(Context context) {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
        return activeInfo != null && activeInfo.isConnected() && activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public static void hideKeyboard(Context context, View tokenView) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                tokenView.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, View tokenView) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tokenView,
                InputMethodManager.SHOW_FORCED);
    }

    public static String generateUUID() {
        return java.util.UUID.randomUUID().toString();
    }

    private static final String JPEG_FILE_PREFIX = "IMG_";
    private static final String JPEG_FILE_SUFFIX = ".jpg";

    private static final String ALBUM_DIR = "lukou";

    private static File mAlbumStorageDir = null;

    public static boolean copyToFile(File srcFile, File destFile) {
        try {
            if (!srcFile.exists()) {
                return false;
            }
            if (destFile.exists()) {
                destFile.delete();
            }
            try (InputStream in = new FileInputStream(srcFile); OutputStream out = new FileOutputStream(destFile)) {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * 是否有启动intent的app
     *
     * @param context
     * @param urls    跳转url列表
     * @return
     */
    public static Pair<Boolean, String> hitUrls(Context context, String... urls) {
        if (ArrayUtils.isEmpty(urls)) {
            return Pair.create(false, null);
        }
        for (String url : urls) {
            Intent intent = new Intent(Intent.ACTION_VIEW, LKUtil.parseUrl(url));
            if (LKUtil.hasAppsStartIntent(context, intent)) {
                return Pair.create(true, url);
            }
        }
        return Pair.create(false, null);
    }

    /**
     * 是否有启动intent的app
     *
     * @param context
     * @param intent
     * @return
     */
    public static boolean hasAppsStartIntent(Context context, Intent intent) {
        List<ResolveInfo> appInfos = getAppsForIntent(context, intent);
        return appInfos != null && appInfos.size() > 0;
    }

    public static List<ResolveInfo> getAppsForIntent(Context context,
                                                     Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(
                intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo;
    }

    public static String getProcessNameByPID(Context context, int pid) {
        if (context == null) {
            return "";
        }
        ActivityManager manager
                = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager.getRunningAppProcesses() == null) {
            return "";
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return "";
    }

    public static int getIntentExtraInt(Intent intent, String key) {
        if (intent == null) {
            return 0;
        }
        int value;
        value = intent.getIntExtra(key, 0);
        if (value == 0) {
            Uri encodeUri = intent.getData();
            if (encodeUri != null && encodeUri.getQueryParameter(key) != null) {
                value = Integer.valueOf(encodeUri.getQueryParameter(key));
            }
        }
        return value;
    }

    public static long getIntentExtraLong(Intent intent, String key) {
        if (intent == null) {
            return 0;
        }
        long value;
        value = intent.getLongExtra(key, 0);
        if (value == 0) {
            Uri encodeUri = intent.getData();
            if (encodeUri != null && encodeUri.getQueryParameter(key) != null) {
                value = Long.valueOf(encodeUri.getQueryParameter(key));
            }
        }
        return value;
    }

    public static String getIntentExtraString(Intent intent, String key) {
        if (intent == null) {
            return "";
        }
        String value;
        value = intent.getStringExtra(key);
        if (TextUtils.isEmpty(value)) {
            Uri encodeUri = intent.getData();
            if (encodeUri != null && encodeUri.getQueryParameter(key) != null) {
                value = Uri.decode(encodeUri.getQueryParameter(key));
            }
        }
        return value;
    }

    //复制到剪贴板
    public static void copyToClipboard(Context context, String content) {
        if (TextUtils.isEmpty(content)) {
            ToastManager.showToast("复制失败～");
            return;
        }
        ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(null, content.trim());
        cmb.setPrimaryClip(clipData);
    }

    /**
     * @param context
     * @return
     */
    public static Activity getActivityFromContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return getActivityFromContext(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    /**
     * 获取double数据的小数的值
     * Examples:
     * 1.01 returns "01"
     * 1.1 returns "1"
     *
     * @param price
     * @return
     */
    public static String getDecimal(double price) {
        String format = "%.2f";

        //获取小数点后数字
        double decimal = price - (long) price;
        String strDec = String.format(Locale.CHINA, format, decimal);
        if (strDec.charAt(strDec.length() - 1) == '0') {
            strDec = strDec.substring(0, strDec.length() - 1);
        }
        return strDec.replace("0.", "");
    }

    /**
     * 获取优惠券价格
     * "30元券" 返回30
     *
     * @param couponValue
     * @return
     */
    private static Pattern pattern = Pattern.compile("[^0-9]");

    public static int getCouponPrice(String couponValue) {
        if (TextUtils.isEmpty(couponValue)) {
            return 0;
        }
        Matcher m = pattern.matcher(couponValue);
        return Integer.valueOf(m.replaceAll(""));
    }

    /**
     * 优惠券hack
     *
     * @param coupon
     * @return
     */
    public static String getCouponStr(String coupon) {
        if (TextUtils.isEmpty(coupon)) {
            return "";
        }
        return "券¥" + coupon.replace("元券", "");
    }

    /**
     * 根据传入数字返回购买人数
     *
     * @param num
     * @return
     */
    public static SpannableString getSpanCusNum(int num) {
        String strNum = String.valueOf(num);
        String resultStr = "已有" + strNum + "人购买";
        SpannableString spanNum = new SpannableString(String.valueOf(resultStr));
        spanNum.setSpan(new ForegroundColorSpan(BaseApplication.instance().getResources().getColor(R.color.colorAccent)), 2, 2 + strNum.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanNum;
    }

    /**
     * 价格整数字号大， 小数字号小
     * 形式为："￥9.99"
     *
     * @param price
     * @return
     */
    public static SpannableString getSpanPrice(double price) {
        return getSpanPrice(price, 0.45f, 0.6f);
    }

    public static SpannableString getSpanPrice(double price, float simbolSize, float decimalSize) {
        String strPrice = "¥" + String.valueOf(price);
        if (strPrice.endsWith(".0")) {
            strPrice = strPrice.substring(0, strPrice.indexOf(".0"));
        }
        SpannableString spanPrice = new SpannableString(String.valueOf(strPrice));
        spanPrice.setSpan(new RelativeSizeSpan(simbolSize), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (strPrice.indexOf('.') >= 0) {
            spanPrice.setSpan(new RelativeSizeSpan(decimalSize), strPrice.indexOf('.'), strPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanPrice;
    }

    /**
     * 价格区间的展示
     *
     * @param minPrice
     * @param maxPrice
     * @return
     */
    public static SpannableString getMinMaxSpanPrice(double minPrice, double maxPrice) {
        if (minPrice == maxPrice) {
            return getSpanPrice(minPrice, 0.5f, 0.83f);
        }
        String strPrice = "¥" + String.valueOf(minPrice) + " ~ " + String.valueOf(maxPrice);
        SpannableString spanPrice = new SpannableString(String.valueOf(strPrice));
        spanPrice.setSpan(new RelativeSizeSpan(0.6f), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        int first = strPrice.indexOf(' ');
        int last = strPrice.lastIndexOf(' ');
        if (strPrice.indexOf('.') < first && strPrice.lastIndexOf('.') > last) {
            spanPrice.setSpan(new RelativeSizeSpan(0.6f), strPrice.indexOf('.'), first, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            spanPrice.setSpan(new RelativeSizeSpan(0.6f), strPrice.lastIndexOf('.'), strPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        } else if (strPrice.indexOf('.') > last) {
            spanPrice.setSpan(new RelativeSizeSpan(0.6f), strPrice.indexOf('.'), strPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanPrice;
    }

    /**
     * 价格整数字号大， 小数字号小
     * 形式为："￥9.99"
     *
     * @param price
     * @return
     */
    public static SpannableString getSmallSpanPrice(double price) {
        String strPrice = "¥" + String.valueOf(price);
        if (strPrice.endsWith(".0")) {
            strPrice = strPrice.substring(0, strPrice.indexOf(".0"));
        }
        SpannableString spanPrice = new SpannableString(String.valueOf(strPrice));
        spanPrice.setSpan(new RelativeSizeSpan(0.8f), 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        if (strPrice.indexOf('.') >= 0) {
            spanPrice.setSpan(new RelativeSizeSpan(0.8f), strPrice.indexOf('.'), strPrice.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        return spanPrice;
    }


    /**
     * @param saleNum
     * @return
     */
    public static String getSaleNum(int saleNum) {
        String format = "%.1f";
        if (saleNum > 10000) {
            double num = saleNum / 10000.0;
            return String.format(Locale.CHINA, format, num) + "万";
        }
        return String.valueOf(saleNum);
    }


    /**
     * 过滤referId 的数字
     *
     * @param referId
     * @return
     */
    public static String getReferExceptId(String referId) {
        if (TextUtils.isEmpty(referId)) {
            return "";
        }
        if (!referId.endsWith("_")) {
            referId += "_";
        }
        return referId;
    }

    public static void restartApp(@NonNull Context context) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(BuildConfig.APPLICATION_ID);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }


    /**
     * 判断包名对应的App是否存在
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppExist(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        List<PackageInfo> pkgList = manager.getInstalledPackages(0);
        for (int i = 0; i < pkgList.size(); i++) {
            PackageInfo packageInfo = pkgList.get(i);
            if (packageInfo.packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * String转MD5
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(str.getBytes());
        return new BigInteger(1, md.digest()).toString(16);
    }

}
