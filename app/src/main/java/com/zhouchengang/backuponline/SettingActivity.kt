package com.zhouchengang.backuponline



import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_setting.*


class SettingActivity : AppCompatActivity(R.layout.activity_setting) {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        //创建一个SharedPreferences.Editor接口对象，user表示要写入的xml文件名
        //var editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
        //editor.putString("ip", "39.106.146.89");
        //editor.commit();
        //删除所有缓存数据
        //editor.clear();


        ip1.setText(FoneActivity.read.getString("ip", ""))
        loginbutton.setOnClickListener {

            var editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            editor.putString("ip", ip1.text.toString());
            editor.putString("pass", passwordedit.text.toString());
            editor.commit();

            val intent = baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent!!.putExtra("REBOOT", "reboot")
            startActivity(intent)

        }

        delbutton.setOnClickListener {
            ip1.setText("")
            passwordedit.setText("")
            var editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit()
            //editor.putString("ip", "");
            //editor.putString("pass", "");
            editor.clear();
            editor.commit()

            FoneActivity.audiolistonserver.clear();

        }

    }


    companion object {
        fun createIntent(context: Context): Intent = Intent(context, SettingActivity::class.java)

    }
}
