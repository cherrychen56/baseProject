package com.magic_chen_.baseproject.utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.magic_chen_.baseproject.GlobalApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;



/**
 * Created by Administrator on 2016/5/26.
 */
public class OtherUtils {

    /**
     * 正则表达式：验证邮箱
     */
    public static final String REGEX_EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    public static final String regExp = "^(?=.*?[a-zA-Z])(?=.*?[0-9])[a-zA-Z0-9_]{6,20}$";


    /**
     * 验证密码是否为6-20位
     */
    public static boolean isPassword(String password) {
        return password.matches(regExp);
    }

    /*// 判断一个字符串是否含有数字
    public static boolean HasDigit(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*\\d+.*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }*/

    // 判断一个字符串是否含有英文
    public static boolean HasLetter(String content) {
        boolean flag = false;
        Pattern p = Pattern.compile(".*[a-zA-z].*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            flag = true;
        }
        return flag;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String telRegex = "[1][356789]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。"[1][358]\\d{9}"
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    /**
     * 加密用，设置七牛上传文件名
     */
    public static String setEncryptFileName() {
        try {
            return "" + "_" + UUID.randomUUID() + "_.jpg";//DateUtils.formatDateBy(new Date(), "yyyy_MM_dd_HHmmss")
        } catch (Exception e) {
            e.printStackTrace();
//            App.getInstance().uploadErrorLogs(e.toString());
        }

        return "";
    }

    public static String setFileName() {
        try {
            return "" + "_" + UUID.randomUUID() + ".jpg";//DateUtils.formatDateBy(new Date(), "yyyy_MM_dd_HHmmss")
        } catch (Exception e) {
            e.printStackTrace();
//            App.getInstance().uploadErrorLogs(e.toString());
        }

        return "";
    }


    /**
     * 校验邮箱
     *
     * @param email
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isEmail(String email) {
        return Pattern.matches(REGEX_EMAIL, email);
    }

    /**
     * 验证身份证格式
     */
    public static boolean isIdCard(String icard) {
        String icardRegex = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$";
        if (TextUtils.isEmpty(icard)) return false;
        else return icard.matches(icardRegex);
    }

    public static void measureListViewHeight(ListView listview, BaseAdapter mychatAdapter) {
        if (mychatAdapter != null) {
            int totalHeight = 0;
            View listItem;
            for (int i = 0; i < mychatAdapter.getCount(); i++) {
                if (mychatAdapter.getCount() > 0) {
                    listItem = mychatAdapter.getView(0, null, listview);
                    listItem.measure(0, 0); //计算子项View 的宽高 //统计所有子项的总高度
                    totalHeight += listItem.getMeasuredHeight() + listview.getDividerHeight();
                }

            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight;
            listview.setLayoutParams(params);

        }

    }

    /**
     * 使用res资源文件显示文本
     */
    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId
                .substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null
                || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }


    public static byte[] encryptData(byte[] data, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            // 编码前设定编码方式及密钥
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 传入编码数据并返回编码结果
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 手机号用****号隐藏中间数字
     *
     * @param phone
     * @return
     */
    public static String settingphone(String phone) {
        String phone_s = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        return phone_s;
    }


    /**
     * 邮箱用****号隐藏前面的字母
     *
     * @return
     */
    public static String settingemail(String email) {
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * 根据图片的url路径获得Bitmap对象
     *
     * @param url
     * @return
     */
    public static Bitmap returnBitmap(String url) {
        URL fileUrl = null;
        Bitmap bitmap = null;

        try {
            fileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) fileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }


    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0") && !nif.getName().equalsIgnoreCase("ccmni0"))
                    continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }


    /**
     * 解析域名获取IP数组
     *
     * @param host
     * @return
     */
    public static String[] parseHostGetIPAddress(String host) {
        String[] ipAddressArr = null;
        try {
            InetAddress[] inetAddressArr = InetAddress.getAllByName(host);
            if (inetAddressArr != null && inetAddressArr.length > 0) {
                ipAddressArr = new String[inetAddressArr.length];
                for (int i = 0; i < inetAddressArr.length; i++) {
                    ipAddressArr[i] = inetAddressArr[i].getHostAddress();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
        return ipAddressArr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


    /**
     * 判断是否安装微信
     *
     * @param context
     * @return
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    private static long time;
    private static int count = 0;

    /**
     * 是否在两秒外连续点击
     *
     * @return
     */
    public static boolean IsTwoSecondClick() {
        int scount = 0;
        if (count == 1 && System.currentTimeMillis() - time > 500) {
            time = System.currentTimeMillis();
            return true;
        } else {
            if (count == scount) {
                time = System.currentTimeMillis();
                count = 1;
                return true;
            } else {
                count = 0;
                return false;
            }
        }
    }

    //过滤数字
    public static String filter(String character) {
        character = character.replaceAll("[^(a-zA-Z\\u4e00-\\u9fa5)\n]", "");
        return character;
    }
    //获取资源文件
//    public static String getFromAssets(String fileName) {
//        String result = "";
//        try {
//            InputStream in = App.app.App.app.getResources().getAssets().open(fileName);
//            // 获取文件的字节数
//            int lenght = in.available();
//            // 创建byte数组
//            byte[] buffer = new byte[lenght];
//            // 将文件中的数据读到byte数组中
//            in.read(buffer);
////            result = EncodingUtils.getString(buffer, ENCODING);
//            result = new String(buffer, "GBK");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断 Uri是否有效
     */
    public static boolean isValidIntent(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
        return !activities.isEmpty();
    }

    //获取当前版本号
    public static int getLocalVersion() {
        int localVersion = 0;
        try {
            PackageInfo packageInfo = GlobalApplication.getGlobalContext()
                    .getPackageManager()
                    .getPackageInfo(GlobalApplication.getGlobalContext().getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    /**
     * 获取本地软件版本号名称
     */
    public static String getLocalVersionName() {
        String localVersion = "";
        try {
            PackageInfo packageInfo = GlobalApplication.getGlobalContext()
                    .getPackageManager()
                    .getPackageInfo(GlobalApplication.getGlobalContext().getPackageName(), 0);
            localVersion = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    public static String getAndroidId() {
        String m_szAndroidID = Settings.Secure.getString(GlobalApplication.getGlobalContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        return m_szAndroidID;
    }

    /**
     * 检查网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager manager = (ConnectivityManager) context
                .getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);

        if (manager == null) {
            return false;
        }

        NetworkInfo networkinfo = manager.getActiveNetworkInfo();

        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }

        return true;
    }

    //判断当前语言环境
    public static boolean isZh() {
        String language = Locale.getDefault().getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    //创建文件
    public static File newFile() {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/" + "Leigod_rule.acl");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 追加文件：使用FileWriter
     *
     * @param fileName
     * @param content
     */
    public static void writeWhilte(String fileName, String content) {
        try {
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void openNewApp(Context context, String appPackage) {
//
//        PackageManager packageManager = context.getPackageManager();
//        Intent intent = new Intent();
//        intent = packageManager.getLaunchIntentForPackage(appPackage);
//        if (intent == null) {
//            Toast.makeText(context, App.app.getString(R.string.install_again), Toast.LENGTH_LONG).show();
//        } else {
//            context.startActivity(intent);
//        }
//    }

    public static void openUrl(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }


    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return 手机IMEI
     */
//    @SuppressLint({"MissingPermission", "HardwareIds"})
//    public static String getIMEI() {
//        String[] perms = {Manifest.permission.READ_PHONE_STATE};
//        if (EasyPermissions.hasPermissions(App.app, perms)) {
//            TelephonyManager tm = (TelephonyManager) App.app.getSystemService(Activity.TELEPHONY_SERVICE);
//            if (tm != null) {
//                String imei = tm.getDeviceId();
//                if (TextUtils.isEmpty(imei)) {
//                    return SharePUtils.getInstance(Constant.LOGIN_INFO).getString(Constant.Android_IMEI, "00000000");
//                }
//                return imei;
//            } else {
//                return SharePUtils.getInstance(Constant.LOGIN_INFO).getString(Constant.Android_IMEI, "00000000");
//            }
//        } else {
//            return SharePUtils.getInstance(Constant.LOGIN_INFO).getString(Constant.Android_IMEI, "00000000");
//        }
//    }

    private static final double EARTH_RADIUS = 6378137.0;

    // 返回单位是米
    public static double getDistance(double longitude1, double latitude1,
                                     double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(Lat1) * Math.cos(Lat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }
//
//    public static GamesInfoModel UpdateGamesInfoModeo(GamesInfoModel lists) {
//        for (GamesInfoModel.GamesModel gamesModel : lists.getGameData()) {
//            for (GamesInfoModel.GamesInfo gamesInfo : lists.getGamesInfos()) {
//                if (gamesInfo.getGameInfos() != null) {
//                    for (GamesInfoModel.GameInfos gameInfos : gamesInfo.getGameInfos()) {
//                        if (gameInfos.getId().equals(gamesModel.getId())) {
//                            switch (gameInfos.getGame()) {
//                                case "LANG_T_China":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_China));
//                                    break;
//                                case "LANG_T_Japan":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Japan));
//                                    break;
//                                case "LANG_T_Korea":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Korea));
//                                    break;
//                                case "LANG_T_Singapore":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Singapore));
//                                    break;
//                                case "LANG_T_US":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_US));
//                                    break;
//                                case "LANG_T_Australia":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Australia));
//                                    break;
//                                case "LANG_T_Germany":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Germany));
//                                    break;
//                                case "LANG_T_UK":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_UK));
//                                    break;
//                                case "LANG_T_France":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_France));
//                                    break;
//                                case "LANG_T_Brazil":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Brazil));
//                                    break;
//                                case "LANG_T_Russian":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Russian));
//                                    break;
//                                case "LANG_T_Italy":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Italy));
//                                    break;
//                                case "LANG_T_India":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_India));
//                                    break;
//                                case "LANG_T_Canada":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Canada));
//                                    break;
//                                case "LANG_T_Spain":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Spain));
//                                    break;
//                                case "LANG_T_Thailand":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Thailand));
//                                    break;
//                                case "LANG_T_Taiwan":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Taiwan));
//                                    break;
//                                case "LANG_T_HongKong":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_HongKong));
//                                    break;
//                                case "LANG_T_Malaysia":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Malaysia));
//                                    break;
//                                case "LANG_T_Philippines":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Philippines));
//                                    break;
//                                case "LANG_T_Indonesia":
//                                    gamesModel.setGame(App.app.getResources().getString(R.string.LANG_T_Indonesia));
//                                    break;
//                                default:
//                                    gamesModel.setGame(gameInfos.getGame());
//                                    break;
//                            }
//                            switch (gameInfos.getInfo()) {
//                                case "LANG_I_China":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_China));
//                                    break;
//                                case "LANG_I_Japan":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Japan));
//                                    break;
//                                case "LANG_I_Korea":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Korea));
//                                    break;
//                                case "LANG_I_Singapore":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Singapore));
//                                    break;
//                                case "LANG_I_US":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_US));
//                                    break;
//                                case "LANG_I_Australia":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Australia));
//                                    break;
//                                case "LANG_I_Germany":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Germany));
//                                    break;
//                                case "LANG_I_UK":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_UK));
//                                    break;
//                                case "LANG_I_France":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_France));
//                                    break;
//                                case "LANG_I_Brazil":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Brazil));
//                                    break;
//                                case "LANG_I_Russian":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Russian));
//                                    break;
//                                case "LANG_I_Italy":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Italy));
//                                    break;
//                                case "LANG_I_India":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_India));
//                                    break;
//                                case "LANG_I_Canada":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Canada));
//                                    break;
//                                case "LANG_I_Spain":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Spain));
//                                    break;
//                                case "LANG_I_Thailand":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Thailand));
//                                    break;
//                                case "LANG_I_Taiwan":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Taiwan));
//                                    break;
//                                case "LANG_I_HongKong":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_HongKong));
//                                    break;
//                                case "LANG_I_Malaysia":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Malaysia));
//                                    break;
//                                case "LANG_I_Philippines":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Philippines));
//                                    break;
//                                case "LANG_I_Indonesia":
//                                    gamesModel.setGame_info(App.app.getResources().getString(R.string.LANG_I_Indonesia));
//                                    break;
//                                default:
//                                    gamesModel.setGame_info(gameInfos.getInfo());
//                                    break;
//                            }
//                            gamesModel.setAlias(gameInfos.getKeyword());
//                        }
//                    }
//                }
//            }
//        }
//        return lists;
//    }


    /**
     * 判断手机是否安装某个应用
     *
     * @param context
     * @param appPackageName 应用包名
     * @return true：安装，false：未安装
     */
    public static boolean isApplicationAvilible(Context context, String appPackageName) {
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (appPackageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }

    //取平均数
    public static float ArithmeticMean(ArrayList<Float> x) {
        int m = x.size();
        float sum = 0;
        for (int i = 0; i < m; i++) {//计算x值的倒数
            sum += x.get(i);
        }
        return sum / m;

    }

    /**
     * 生成一个0 到 count 之间的随机数
     *
     * @param endNum
     * @return
     */
    public static int getNum(int endNum) {
        if (endNum > 0) {
            Random random = new Random();
            return random.nextInt(endNum) + 1;
        }
        return 0;
    }

    /**
     * 生成一个startNum 到 endNum之间的随机数(不包含endNum的随机数)
     *
     * @param startNum
     * @param endNum
     * @return
     */
    public static int getNum(int startNum, int endNum) {
        if (endNum > startNum) {
            Random random = new Random();
            return random.nextInt(endNum - startNum) + startNum;
        }
        return 0;
    }

    /**
     * 秒转换为时分
     */
    public static String formatTime(long ms) {
        int ss = 1;
        int mi = ss * 60;
        int hh = mi * 60;
        long hour = ms / hh;
        long minute = (ms - hour * hh) / mi;
        long second = ms - hour * hh - minute * mi;
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        String strSecond = second < 10 ? "0" + second : "" + second;//秒
        return strHour + ":" + strMinute + ":" + strSecond;
    }


    //判断是否是wifi
    public static boolean isWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) GlobalApplication.getGlobalContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

//Md5加密
//    public static String md5(String content) {
//        byte[] hash;
//        try {
//            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException("NoSuchAlgorithmException",e);
//        } catch (UnsupportedEncodingException e) {
//            throw new RuntimeException("UnsupportedEncodingException", e);
//        }
//
//        StringBuilder hex = new StringBuilder(hash.length * 2);
//        for (byte b : hash) {
//            if ((b & 0xFF) < 0x10){
//                hex.append("0");
//            }
//            hex.append(Integer.toHexString(b & 0xFF));
//        }
//        return hex.toString();
//    }
}
