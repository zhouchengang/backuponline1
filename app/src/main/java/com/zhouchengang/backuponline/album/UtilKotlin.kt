package com.zhouchengang.backuponline.album

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.*
import java.math.BigInteger
import java.net.URL
import java.security.MessageDigest
import java.util.*

class UtilKotlin {

    companion object {
        const val BASE_URL = "http://192.168.8.247:4545/"
        const val IMG_URL = "http://47.240.168.158:8888/"

        const val UP_URL = "http://192.168.0.113:3000/"

        @JvmStatic
        fun isDir(s: String?): Boolean {
            return if (null == s || s.isEmpty()) {
                false
            } else s[s.length - 1] == '/'
        }

        @JvmStatic
        fun isNotDir(s: String?): Boolean {
            return if (null == s || s.isEmpty()) {
                false
            } else s[s.length - 1] != '/'
        }

        var ip = "47.242.155.156"
        const val TAG = "uploadFile"
        const val TIME_OUT = 10 * 1000 //超时时间

        const val CHARSET = "utf-8" //设置编码

        const val REQUEST_EXTERNAL_STORAGE = 1
        val PERMISSIONS_STORAGE = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        @JvmStatic
        fun getDirByPath(path: String) = path.substring(0, path.lastIndexOf('/') + 1)

        @JvmStatic
        fun getNameByPath(path: String) = path.substring(path.lastIndexOf('/') + 1)

        @JvmStatic
        fun getCoverUrl(path: String) = "http://192.168.0.113:8889/$path"

        @JvmStatic
        fun getPicUrl(path: String) = "http://192.168.0.113:8888/$path"

        @JvmStatic
        fun path2Name(path: String) = path.substring(path.lastIndexOf("/") + 1)

        @JvmStatic
        fun path2Dir(path: String) = path.substring(0, path.lastIndexOf("/") + 1);

    }

    /**
     * 在对sd卡进行读写操作之前调用这个方法
     * Checks if the app has permission to write to device storage
     * If the app does not has permission then the user will be prompted to grant permissions
     */
    fun verifyStoragePermissions(activity: Activity?) {
        val permission = ActivityCompat.checkSelfPermission(
            activity!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS_STORAGE,
                REQUEST_EXTERNAL_STORAGE
            )
        }
    }

    /**
     * 获取cache路径
     */
    private fun getDiskCachePath(sContext: Context): String? {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState() || !Environment.isExternalStorageRemovable()) {
            sContext.externalCacheDir!!.path
        } else {
            sContext.cacheDir.path
        }
    }


    fun getFileMD5s(file: File, radix: Int): String? {
        if (!file.isFile) {
            return null
        }
        var digest: MessageDigest? = null
        var fileInputStream: FileInputStream? = null
        val buffer = ByteArray(1024)
        var len: Int
        try {
            digest = MessageDigest.getInstance("MD5")
            fileInputStream = FileInputStream(file)
            while (fileInputStream.read(buffer, 0, 1024).also { len = it } != -1) {
                digest.update(buffer, 0, len)
            }
            fileInputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
            return "null"
        }
        val bigInt = BigInteger(1, digest.digest())
        return "" + bigInt.toString(radix)
    }

    fun sjc2str(sjc: String?): String? {
        val tms = Calendar.getInstance()
        tms.timeInMillis = java.lang.Long.valueOf(sjc!!)
        return (tms[Calendar.YEAR].toString() + "-" + (tms[Calendar.MONTH] + 1)
                + "-" + tms[Calendar.DAY_OF_MONTH] + " " + tms[Calendar.HOUR_OF_DAY]
                + ":" + tms[Calendar.MINUTE]
                + ":" + tms[Calendar.SECOND])
    }


    fun writeMessage2Local(logName: Long, content: String, sContext: Context?) {
        sContext ?: return
        //if(true) return ;
        Log.e("write", content)
        val localpath = getDiskCachePath(sContext)
        val filedir = File("$localpath/record/")
        if (!filedir.exists()) {
            Log.d("TAG", "dir no exists")
            filedir.mkdirs()
        }
        var out: BufferedWriter? = null
        try {
            out = BufferedWriter(
                OutputStreamWriter(
                    FileOutputStream(localpath + "/record/" + logName / (1000 * 60) + ".txt", true)
                )
            )
            out.write("\r\n") //写入换行
            out.write("$logName  $content")
            out.write("-------")
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        } finally {
            try {
                out!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * 将InputStream写入本地文件
     */
    @Throws(IOException::class)
    private fun writeToLocal(destinationDir: String, fileName: String, input: InputStream) {
        val file = File(destinationDir)
        if (!file.exists()) {
            Log.d("TAG", "dir no exists")
            file.mkdirs()
        }
        var index: Int
        val bytes = ByteArray(1024)
        val downloadFile = FileOutputStream(destinationDir + fileName)
        while (input.read(bytes).also { index = it } != -1) {
            downloadFile.write(bytes, 0, index)
            downloadFile.flush()
        }
        downloadFile.close()
        input.close()
    }

    /**
     * 从服务器下载文件
     */
    fun downloadFile(path: String, sContext: Context) {
        val picFileName = path2Name(path)
        val path2 = path2Dir(path)
        val localPath = getDiskCachePath(sContext)
        try {
            val url = URL("http://$ip:8888/$path2/$picFileName")
            val iis = url.openStream()
            writeToLocal("$localPath/uploads/$path2/", picFileName, iis)
            iis.close()
            moveFile("$localPath/uploads/$path2/$picFileName", path2, picFileName, sContext)
            return
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return
    }


    /**
     * 复制单个文件
     */
    private fun moveFile(oldPath: String?, dir: String, fileName: String, sContext: Context) {
        val file = File(dir)
        if (!file.exists()) {
            file.mkdirs()
        }
        try {
            val oldFile = File(oldPath)
            if (!oldFile.exists() || !oldFile.isFile || !oldFile.canRead()) {
                return
            }
            val fileInputStream = FileInputStream(oldPath)
            val fileOutputStream = FileOutputStream(dir + fileName)
            val buffer = ByteArray(1024)
            var byteRead: Int
            while (-1 != fileInputStream.read(buffer).also { byteRead = it }) {
                fileOutputStream.write(buffer, 0, byteRead)
            }
            fileInputStream.close()
            fileOutputStream.flush()
            fileOutputStream.close()
            val file1 = File(oldPath)
            file1.delete()
            sContext.sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse("file://$dir$fileName")
                )
            )
            return
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}