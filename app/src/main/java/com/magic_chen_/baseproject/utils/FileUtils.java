package com.magic_chen_.baseproject.utils;

import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import com.magic_chen_.baseproject.GlobalApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;


/**
 * Created by magic_chen_ on 2018/8/24.
 * email:chenyouya@leigod.com
 * project:BoheAccelerator_Android
 */
public class FileUtils {
    public static final String APP_FILE = "appFile";
    public static final int GLOBAL_BUFFER_SIZE = 512 * 1024;

    public static void makesureParentExist(File file_) {
        File parent = file_.getParentFile();
        if ((parent != null) && (!parent.exists()))
            mkdirs(parent);
    }

    public static void makesureParentExist(String filepath_) {
        makesureParentExist(new File(filepath_));
    }

    public static void makesureFileExist(File file_) {
        if (!file_.exists()) {
            makesureParentExist(file_);
            createNewFile(file_);
        }
    }

    public static void mkdirs(File dir_) {
        if ((!dir_.exists()) && (!dir_.mkdirs()))
            throw new RuntimeException("fail to make " + dir_.getAbsolutePath());
    }

    public static void createNewFile(File file_) {
        if (!__createNewFile(file_))
            throw new RuntimeException(file_.getAbsolutePath()
                    + " doesn't be created!");
    }

    private static boolean __createNewFile(File file_) {
        makesureParentExist(file_);
        if (file_.exists())
            delete(file_);
        try {
            return file_.createNewFile();
        } catch (IOException e) {

        }

        return false;
    }

    public static void delete(File f) {
        if ((f != null) && (f.exists()) && (!f.delete()))
            throw new RuntimeException(f.getAbsolutePath()
                    + " doesn't be deleted!");
    }

    public static void delete(String path) {
        File f = new File(path);
        if ((f != null) && (f.exists()) && (!f.delete()))
            throw new RuntimeException(f.getAbsolutePath()
                    + " doesn't be deleted!");
    }

    public static void makesureFileExist(String filePath_) {
        makesureFileExist(new File(filePath_));
    }

    public static FileInputStream getFileInputStream(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {

        }

        return fis;
    }

    public static FileInputStream getFileInputStream(String path) {
        return getFileInputStream(new File(path));
    }


    public static boolean closeStream(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
                return true;
            }
        } catch (IOException e) {
        }

        return false;
    }

    public static InputStream makeInputBuffered(InputStream input_) {
        if ((input_ instanceof BufferedInputStream)) {
            return input_;
        }
        return new BufferedInputStream(input_, GLOBAL_BUFFER_SIZE);
    }

    public static OutputStream makeOutputBuffered(OutputStream output_) {
        if ((output_ instanceof BufferedOutputStream)) {
            return output_;
        }

        return new BufferedOutputStream(output_, GLOBAL_BUFFER_SIZE);
    }


    public static long getFileSize(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }

        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            return 0;
        }
    }

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    public static boolean isFolderExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isDirectory());
    }

    public static boolean isFileExist(File file) {
        return (file.exists() && file.isFile());
    }

    public static void copy(String pathIn_, String pathOut_) throws IOException {
        copy(new File(pathIn_), new File(pathOut_));
    }

    public static void copy(File in_, File out_) throws IOException {

        makesureParentExist(out_);
        copy(new FileInputStream(in_), new FileOutputStream(out_));
    }

    public static void copy(InputStream input_, OutputStream output_)
            throws IOException {
        try {
            byte[] buffer = new byte[GLOBAL_BUFFER_SIZE];
            int temp = -1;
            input_ = makeInputBuffered(input_);
            output_ = makeOutputBuffered(output_);
            while ((temp = input_.read(buffer)) != -1) {
                output_.write(buffer, 0, temp);
                output_.flush();
            }
        } catch (IOException e) {
            throw e;
        } finally {
          closeStream(input_);
          closeStream(output_);
        }
    }


    public static String getBaseDataDir() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getPath();
        } else {

            return GlobalApplication.getGlobalContext().getFilesDir().getPath();
        }
    }


    public static String getCrashFileDir() {
        String dir = getBaseDataDir();
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        dir = dir + "Android/data/com.leigod.accelerator/crash";
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }

        return dir + File.separator;
    }


    public static String getLogDir() {
        String dir = getBaseDataDir();
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        dir = dir + "Android/data/com.leigod.accelerator/Log";
        File file = new File(dir);
        if (!file.exists())
            file.mkdirs();
        return dir + File.separator;
    }

    public static String getPicSaveDir() {
        String dir = getBaseDataDir();
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        dir = dir + "bohe/";
        return dir;
    }

    public static void saveLog(String str) {
        String path = getLogDir() + "log-" + System.currentTimeMillis() + ".txt";
        saveFile(path, str);
    }


    public static String saveCrashLog(String str) {
        String path = getCrashFileDir() + "crash-" + System.currentTimeMillis() + ".txt";
        saveFile(path, str);

        return path;
    }

    public static void saveFile(String filePath, String content) {
        File file = null;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            br = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.getBytes())));
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));

            String bStr = null;
            while ((bStr = br.readLine()) != null) {
                bw.write(bStr);
                bw.write("\n");
            }
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getAppDownloadFileDir() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String dir = GlobalApplication.getGlobalContext().getExternalFilesDir(APP_FILE).getPath();
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return dir;
        } else {
            String dir = getBaseDataDir();
            if (!dir.endsWith(File.separator)) {
                dir = dir + File.separator;
            }
            dir += APP_FILE;
            File file = new File(dir);
            if (!file.exists()) {
                file.mkdirs();
            }
            return dir;
        }
    }

    /**
     * 将一个inputStream里面的数据写到SD卡中
     *
     * @param path
     * @param fileName
     * @param inputStream
     * @return
     */
    public static File writeToSDfromInput(String path, String fileName, InputStream inputStream) {
        File file = new File(path + fileName);

        OutputStream outStream = null;
        try {
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            outStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            while (inputStream.read(buffer) != -1) {
                outStream.write(buffer);
            }
            outStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null)
                    outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void deleteFile(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }
        deleteFile(new File(path));
    }

    public static void deleteFile(File rootFile) {
        if (rootFile == null || !rootFile.exists()) {
            return;
        }

        if (rootFile.isDirectory()) {
            File[] files = rootFile.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteFile(file);
                }
            }
        }
        rootFile.delete();
    }

    public static boolean compare(File file1, File file2) {
        if (file1 == null || !file1.exists() || !file1.isFile()) {
            return false;
        }
        if (file2 == null || !file2.exists() || !file2.isFile()) {
            return false;
        }

        long fileLen1 = file1.length();
        long fileLen2 = file2.length();
        if (fileLen1 != fileLen2) {
            return false;
        }

        boolean isEqual = false;
        FileInputStream fileInputStream1 = null;
        FileInputStream fileInputStream2 = null;

        try {
            fileInputStream1 = new FileInputStream(file1);
            fileInputStream2 = new FileInputStream(file2);
            byte[] readBuffer1 = new byte[2048];
            byte[] readBuffer2 = new byte[2048];

            int readLen1 = fileInputStream1.read(readBuffer1);
            int readLen2 = fileInputStream2.read(readBuffer2);
            while (readLen1 == readLen2 && readLen1 != -1) {
                int i = 0;
                while (i < readLen1 && readBuffer1[i] == readBuffer2[i]) {
                    i++;
                }
                if (i < readLen1) {
                    break;
                }
                readLen1 = fileInputStream1.read(readBuffer1);
                readLen2 = fileInputStream2.read(readBuffer2);
            }
            isEqual = (readLen1 == -1 && readLen2 == -1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream1 != null) {
                    fileInputStream1.close();
                }
                if (fileInputStream2 != null) {
                    fileInputStream2.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isEqual;
    }

    public static String getFileContent(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStreamReader isr = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String next = "";
            while ((next = br.readLine()) != null) {
                stringBuilder.append(next);
            }

            isr.close();
        } catch (Exception e) {
            e.printStackTrace();
            stringBuilder.delete(0, stringBuilder.length());
        }


        return stringBuilder.toString();
    }

    public static String ReadTxtFile(String strFilePath)
    {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.d("FileUtils", "The File doesn't not exist.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                    instream.close();
                }
            }
            catch (FileNotFoundException e)
            {
                Log.d("FileUtils", "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.d("FileUtils", e.getMessage());
            }
        }
        return content;
    }
}
