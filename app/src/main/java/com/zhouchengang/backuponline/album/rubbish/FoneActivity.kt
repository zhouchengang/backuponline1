//package com.zhouchengang.backuponline.album
//
//
//import android.content.*
//import android.content.pm.ActivityInfo
//import android.content.res.Configuration
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.view.View
//import android.webkit.WebChromeClient
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.zhouchengang.backuponline.*
//import com.zhouchengang.backuponline.album.UploadUtil.downloadFile
//import com.zhouchengang.backuponline.album.UploadUtil.uploadFile
//import com.zhouchengang.fileonlinelaunchapp.R
//import kotlinx.android.synthetic.main.activity_fone.*
//import kotlinx.coroutines.GlobalScope
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//
//
//class FoneActivity : AppCompatActivity() {
//
//
//    fun renew1() {
//        message1.apply {
//            layoutManager =
//                LinearLayoutManager(this@FoneActivity, RecyclerView.VERTICAL, false)
//            setHasFixedSize(true)
//            setLayoutManager(layoutManager)
//            adapter =
//                SelectionChatCardAdapter(
//                    audiolistonserver
//                )
//            //scrollToPosition((adapter as SelectionChatCardAdapter).getItemCount()-1);
//            scrollToPosition(0);
//        }
//
//    }
//
//    fun renew2() {
//        message2.apply {
//            layoutManager =
//                LinearLayoutManager(this@FoneActivity, RecyclerView.VERTICAL, false)
//            setHasFixedSize(true)
//            setLayoutManager(layoutManager)
//            adapter =
//                SelectionChatCardAdapter2(
//                    audiolistonserver
//                )
//            //scrollToPosition((adapter as SelectionChatCardAdapter).getItemCount()-1);
//            scrollToPosition(0);
//        }
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//
//        //var  window  = getWindow();
//        //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
//        //window.setStatusBarColor(Color.TRANSPARENT);
//        sContext = this
//        audiolistonserver = HashMap<String, Int>()
//
//
//        //????????????SharedPreferences.Editor???????????????user??????????????????xml?????????
//        //var editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
//        //editor.putString("ip", "39.106.146.89");
//        //editor.commit();
//        //????????????????????????
//        //editor.clear();
//
//
//        read = getSharedPreferences("user", Context.MODE_PRIVATE)
//        var iip = "" + read.getString("ip", "")
//        Log.e("read,iip: ", "" + iip)
//
//        setContentView(R.layout.activity_fone)
//        //texting.text="???????????????"
//
//
//        registerBoradcastReceiver()
//        getSupportActionBar()?.hide()
//        UploadUtil.verifyStoragePermissions(this)
//
//        val intent1 = Intent(
//            this@FoneActivity,
//            MainService().javaClass
//        )
//        startService(intent1)
//
//
//
//        setSupportActionBar(toolbar)
//
//        upallbutt.setOnClickListener {
//            runBlocking {
//                GlobalScope.launch {
//                    for ((index, element) in audiolistonserver) {
//                        if (element == 5) {
//                            uploadFile("" + index)
//                            audiolistonserver.put(index, 2);
//                        }
//                    }
//                }
//            }
//        }
//
//        text1.text = "?????????????????????"
//        text2.text = "?????????????????????"
//        downallbutt.setOnClickListener {
//            object : Thread() {
//                override fun run() {
//                    for ((index, element) in audiolistonserver) {
//                        if (element == 0) {
//                            println("begin: " + index);
//                            downloadFile(index)
//                            println("over : " + index);
//                            audiolistonserver.put(index, 2);
//                        }
//                    }
//                }
//            }.start()
//        }
//
//
//
//        closebutt.setOnClickListener {
//            webpage.loadUrl("about:blank");
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
//            vidd.visibility = View.INVISIBLE
//        }
//        vidd.visibility = View.INVISIBLE
//
//        hsbutt.setOnClickListener {
//            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                Log.i("ToVmp", "??????");
//            } else {
//                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                Log.i("ToVmp", "??????");
//            }
//        }
//
//        enableSwitch2.isChecked =
//            sureupload
//        enableSwitch2.setOnClickListener {
//            Log.e("enableSwitch2", "" + enableSwitch2.isChecked)
//            sureupload = enableSwitch2.isChecked
//        }
//
//        enableSwitch.isChecked =
//            suredownload
//        enableSwitch.setOnClickListener {
//            Log.e("enableSwitch", "" + enableSwitch.isChecked)
//            suredownload = enableSwitch.isChecked
//        }
//
//        enableSwitch4.isChecked =
//            sureupload2
//        enableSwitch4.setOnClickListener {
//            sureupload2 = enableSwitch4.isChecked
//        }
//
//        enableSwitch3.isChecked =
//            suredownload2
//        enableSwitch3.setOnClickListener {
//            suredownload2 = enableSwitch3.isChecked
//        }
//
//        toolbar.inflateMenu(R.menu.menu_action_bar)
//
//
//        /////////////////
//        renew1();
//        renew2();
//
//        //?????????????????????
//        webpage.setWebChromeClient(object : WebChromeClient() {
//            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
//                super.onShowCustomView(view, callback);
//            }
//
//            override fun onHideCustomView() {
//                super.onHideCustomView();
//            }
//        })
//
//        Log.e("FOne", "here")
//        super.onCreate(savedInstanceState)
//
//    }
//
//
//    fun registerBoradcastReceiver() {
//        val filer = IntentFilter() //????????????
//        //?????????????????????????????????
//        filer.addAction("ACTION_GOTODRAW")
//        filer.addAction("ACTION_WEBRENEW")
//        registerReceiver(mBroadcastReceiver, filer)
//        Log.e("FoneActivity", "=== broadcast regist ===")
//    }
//
//    var mBroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent) {
//            Log.e("myp", "=== broadcast callback ===")
//            val action = intent.action
//            if (action == "ACTION_GOTODRAW") { //???????????????JWEB???
//                val result = intent.getStringExtra("yaner")
//                Log.e("Broadcast", "ACTION_GOTODRAW")
//
//
//                //window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                //window.setStatusBarColor(Color.parseColor("#6200EE"));
//
//                renew1();
//                renew2();
//
//
//            }
//            if (action == "ACTION_WEBRENEW") { //???????????????JWEB???
//                val result = intent.getStringExtra("yaner")
//                Log.e("Broadcast result", "" + result)
//
//                webpage.loadUrl(result)
//                webpage.getSettings().setJavaScriptEnabled(true);
//                webpage.getSettings().setSupportZoom(true);
//
//
//                webpage.getSettings().setUseWideViewPort(true);
//                webpage.getSettings().setLoadWithOverviewMode(true);
//                webpage.getSettings().setAllowFileAccess(true);
//                webpage.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//
//                vidd.visibility = View.VISIBLE
//
//            }
//        }
//
//
//    }
//
//
//    override fun finish() {
//        Log.e("FoneActivity", "Try to finish()");
//        moveTaskToBack(true);//activity
//    }
//
//
//    override fun onStop() {
//        super.onStop()
//        Log.e("FoneActiv", "onstop")
//    }
//
//
//    override fun onResume() {
//        super.onResume()
//        renew1();
//        renew2();
//
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_action_bar, menu)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.action_help -> {
//                var iIntent = Fone2Activity.createIntent(sContext)
//                iIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(iIntent)
//                true
//            }
//
//            R.id.action_settings -> {
//                var iIntent = SettingActivity.createIntent(sContext)
//                iIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(iIntent)
//                true
//            }
//
//            R.id.action_info -> {
//                renew1()
//                renew2()
//                var i0 = 0;
//                var i1 = 0;
//                var i2 = 0;
//                var i4 = 0;
//                var i5 = 0;
//                for ((index, element) in audiolistonserver) {
//                    if (element == 0) i0++;
//                    if (element == 1) i1++;
//                    if (element == 2) i2++;
//                    if (element == 4) i4++;
//                    if (element == 5) i5++;
//                }
//                println("" + i0 + "," + i1 + "," + i2 + ",," + i4 + "," + i5)
//                Toast.makeText(
//                    this,
//                    "???????????????" + i0 + "\n????????????" + i1 + "\n???????????????" + i5 + "\n????????????" + i4 + "\n?????????" + i2,
//                    Toast.LENGTH_SHORT
//                ).show();
//                true
//            }
//            android.R.id.home -> {
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }
//
//    override fun onBackPressed() {
//        if (vidd.visibility == View.INVISIBLE) {
//            var home = Intent(Intent.ACTION_MAIN);
//            //home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//        } else {
//            webpage.loadUrl("about:blank");
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
//            vidd.visibility = View.INVISIBLE
//        }
//    }
//
//
//    companion object {
//        fun createIntent(context: Context): Intent = Intent(context, FoneActivity::class.java)
//        lateinit var audiolistonserver: HashMap<String, Int>
//        lateinit var sContext: Context
//
//        lateinit var read: SharedPreferences
//
//        var sureupload: Boolean = false
//        var sureupload2: Boolean = true
//        var suredownload: Boolean = false
//        var suredownload2: Boolean = true
//    }
//}