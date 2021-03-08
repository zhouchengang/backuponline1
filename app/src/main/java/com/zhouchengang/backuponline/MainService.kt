package com.zhouchengang.backuponline


import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Build
import android.os.IBinder
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.NotificationCompat
import org.java_websocket.handshake.ServerHandshake
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.URI
import java.util.*

class MainService : Service() {
    var inuploading =0;

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    lateinit  var client: JWebSocketClient

    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        if (intent.action == "start") {

        }

        registerBoradcastReceiver()

        /*var iIntent =FoneActivity.createIntent(FoneActivity.sContext)
        iIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iIntent)*/

        initSocketClient()

        mResultCode = intent.getIntExtra("code", -1)
        mResultData = intent.getParcelableExtra<Intent>("data")

        if (mResultCode == 0 || mResultData == null) {
            Log.e("ForgroundService", "Error: Screen record permission is rejected by user")
        }

        //createNotification()

        return  super.onStartCommand(intent, flags, startId)
    }

    fun renewlocalmedia(){
        var cursor: Cursor? = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val data: ByteArray =
                    cursor.getBlob(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                var pathh = String(data, 0, data.size - 1);
                if (FoneActivity.audiolistonserver.contains(pathh)) {
                    FoneActivity.audiolistonserver.put(pathh, 2)
                } else {
                    FoneActivity.audiolistonserver[pathh] = 5; }
            }
        }

        var cursor2: Cursor? = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor2 != null) {
            while (cursor2.moveToNext()) {
                val data: ByteArray =
                    cursor2.getBlob(cursor2.getColumnIndex(MediaStore.Images.Media.DATA))
                var pathh = String(data, 0, data.size - 1);
                if (FoneActivity.audiolistonserver.contains(pathh)) {
                    FoneActivity.audiolistonserver.put(pathh, 2)
                } else {
                    FoneActivity.audiolistonserver[pathh] = 5; }
            }
        }

        var cursor3: Cursor? = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null
        )
        if (cursor3 != null) {
            while (cursor3.moveToNext()) {
                val data: ByteArray =
                    cursor3.getBlob(cursor3.getColumnIndex(MediaStore.Images.Media.DATA))
                var pathh = String(data, 0, data.size - 1);
                if (FoneActivity.audiolistonserver.contains(pathh)) {
                    FoneActivity.audiolistonserver.put(pathh, 2)
                } else {
                    FoneActivity.audiolistonserver[pathh] = 5; }
            }
        }
        for ((index, element) in FoneActivity.audiolistonserver) {
            if (element == 0) {
                val file = File(index)
                if (file.exists()) {
                    FoneActivity.audiolistonserver.put(index, 2);
                }
            }
        }
    }


    /* 初始化websocket连接*/
    private fun initSocketClient() {

        val uri = URI.create("ws://" +FoneActivity.read.getString("ip", "") + ":7877")
        client = object : JWebSocketClient(uri) {
            override fun onMessage(message: String) {
                //Log.e("JWEB", "收到的消息：" + message);
                var objgot: JSONObject? = null
                try {
                    objgot = JSONObject(message)
                    if (objgot.getString("type") == "1001") {

                        //Log.e("jweb","type = 1001");
                        val mIntent = Intent("ACTION_HANDSHAKESUCCESS")
                        if (objgot.getString("result") == "0") {
                            mIntent.putExtra("yaner", "登录成功！！！") //接收到广播时，携带的数据
                        } else {
                            mIntent.putExtra("yaner", "登录失败！！！") //接收到广播时，携带的数据
                        }
                        sendBroadcast(mIntent)
                    }
                    if (objgot.getString("type") == "1002") {
                        //Log.e("jweb", "type = 1002")
                        FoneActivity.audiolistonserver[objgot.getString("path2") + objgot.getString(
                            "filename"
                        )] = 0
                    }
                    if (objgot.getString("type") == "10021") {
                        //Log.e("jweb", "type = 10021")
                        val mIntent = Intent("ACTION_RECEIVEAUDIOFILEINFO")
                        mIntent.putExtra("yaner", "全部接收完毕") //接收到广播时，携带的数据
                        sendBroadcast(mIntent)
                    }
                } catch (e: JSONException) {
                    //Log.e("JWEB","not jsaon");
                    e.printStackTrace()
                }
            }
            override fun onOpen(handshakedata: ServerHandshake) {
                super.onOpen(handshakedata)
            }
        }
        connectt()
    }
    /*连接websocket*/
    private fun connectt() {
        try {
            var rrr = client.connectBlocking()
            Log.e("rrr = ",""+rrr)
            if(rrr){
                val mIntent = Intent("ACTION_INITSUCCESS")
                mIntent.putExtra("yaner", "JWEB INIT完毕") //接收到广播时，携带的数据
                sendBroadcast(mIntent)
            }else{
                renewlocalmedia()

                val mIntent = Intent("ACTION_GOTODRAW")
                mIntent.putExtra("yaner", "刷新本地完毕") //接收到广播时，携带的数据
                sendBroadcast(mIntent)

            }
        } catch (e: InterruptedException) {

            e.printStackTrace()
        }
    }
    /* 发送消息*/
    fun  sendMsg(msg: String?)  :Boolean  {
        try {
            client.send(msg)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
        return true;
    }


    /*断开连接*/
    private fun closeConnect() {
        try {
            client.close()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    fun registerBoradcastReceiver() {
        val filer = IntentFilter() //筛选条件
        //设置要注册的广播的标签
        filer.addAction("ACTION_INITSUCCESS") //登录成功
        filer.addAction("ACTION_MEDIA_SCANNER_SCAN_FILE") //相册更新
        filer.addAction("ACTION_HANDSHAKESUCCESS") //登录成功
        filer.addAction("ACTION_RECEIVEAUDIOFILEINFO") //获得了一条音乐信息
        registerReceiver(mBroadcastReceiver, filer)
        Log.e("myp", "=== broadcast regist ===")
    }
    var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            Log.e("myp", "=== broadcast callback ===")
            val action = intent.action
            if (action == "ACTION_INITSUCCESS") { //成功初始化JWEB，
                val result = intent.getStringExtra("yaner")
                Log.e("Fone",""+result)

                try{
                    var jsonOne2 = JSONObject()
                    jsonOne2.put("type", 1)
                    jsonOne2.put("password", FoneActivity.read.getString("pass", "123"))
                    sendMsg(""+jsonOne2)
                }catch(e:Exception){
                }
            }
            if (action == "ACTION_MEDIA_SCANNER_SCAN_FILE") { //相册跟新
                val result = intent.getStringExtra("yaner")
                Log.e("Fone",""+result)
                Log.e("Fone","相册跟新")


            }
            if (action == "ACTION_HANDSHAKESUCCESS") { //登陆成功
                val result = intent.getStringExtra("yaner")
                Log.e("Fone",""+result)

                /*var iIntent =FoneActivity.createIntent(sContext)
                iIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(iIntent)*/

                        try{
                            FoneActivity.audiolistonserver.clear()
                            var jsonOne2 = JSONObject()
                            jsonOne2.put("type", 2)
                            sendMsg(""+jsonOne2)

                        }catch(e:Exception){
                        }

            }

            if (action == "ACTION_RECEIVEAUDIOFILEINFO") { //接受完毕
                val result = intent.getStringExtra("yaner")
                Log.e("Fone", "" + result)

                try{
                    renewlocalmedia();
                    closeConnect()

                    val mIntent = Intent("ACTION_GOTODRAW")
                    mIntent.putExtra("yaner", "刷新本地完毕") //接收到广播时，携带的数据
                    sendBroadcast(mIntent)


                    onDestroy()

                }catch(e:Exception){
                }
            }


        }
    }
    private var mResultCode = 0
    private var mResultData: Intent? = null


    private fun createNotification() {
        val CHANNEL_ID = "ForegroundServiceChannel"  // Notification Channel ID.
        val input = "Screen Projection Started."
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
        val notificationIntent = Intent(this, FoneActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
    }

    override fun onDestroy() {

        //android.os.Process.killProcess(android.os.Process.myPid());
        Log.e("service","destory")
        closeConnect()
        UploadUtil.writemessagetolocal(
            Calendar.getInstance().timeInMillis,
            "SERVICE 1 ONDESTROY"
        )
        unregisterReceiver(mBroadcastReceiver)
        super.onDestroy()
    }

    companion object {
    }

}