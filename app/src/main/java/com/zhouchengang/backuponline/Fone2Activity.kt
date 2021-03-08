package com.zhouchengang.backuponline



import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.synthetic.main.activity_fone2.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


class Fone2Activity : AppCompatActivity(R.layout.activity_fone2) {


    override fun onCreate(savedInstanceState: Bundle?) {

        var textsaved =""
        super.onCreate(savedInstanceState)

        var myHandler1: Handler = object : Handler() {
            override fun handleMessage(msg: Message) {
                text2.text = textsaved
            }
        }


        upallbutt.setOnClickListener {


            runBlocking {
                GlobalScope.launch {

                    textsaved = "生成md5中"
                    val msg1 = Message()
                    myHandler1.sendMessage(msg1)

                    var la9 = HashMap<Int, item>()
                    var ii = 0;

                    for ((index, element) in FoneActivity.audiolistonserver) {
                        if (element == 5||element == 2) {
                            la9.put(ii,
                                item(
                                    index,
                                    "",
                                    File(index).length()
                                )
                            )
                            ii++
                        }
                    }

                    //var la8= la9.toList().sortedBy { it.second.name}.toMap()
                    var la7 = HashMap<Int, item>()

                    var mmm= 0
                    for ((index, element) in la9) {
                        var totalsize =0
                        for ((i, e) in la9){
                            if(e.len == element.len)
                                totalsize++;
                        }
                        if(totalsize > 1){
                            la7.put(mmm,element)
                            mmm++
                        }
                    }


                    var i=0;
                    var lab0 = HashMap<Int, item>()
                    for ((index, element) in la7) {
                            var md = "" + UploadUtil.getFileMD5s(
                                File(element.name),
                                16
                            )
                            lab0.put(i,
                                item(
                                    element.name,
                                    md,
                                    element.len
                                )
                            )
                            i++
                            textsaved += "\n\n生成md5中" + "\n" + "第" + i + "个\n" + element.name + "\n" + "md5:\n"+ md+"\n"+"len: "+element.len
                            val msg2 = Message()
                            myHandler1.sendMessage(msg2)
                    }

                    //var lab1 = lab0.toList().sortedBy { it.second.md }.toMap()


                    var lab2 = HashMap<Int, item>()
                    var j = 0;
                    for ((index, element) in lab0) {
                        lab2.put(j, element)
                        j++
                    }


                    textsaved +=""

                    val msg3 = Message()
                    myHandler1.sendMessage(msg3)

                    for ((index, element) in lab2) {
                        var thismd = element.md
                        var biggestindex = 0;
                        for ((i, e) in lab2) {
                            if (thismd.equals(e.md))
                                biggestindex = i
                        }

                        if (biggestindex > index && thismd.isNotEmpty()&& (!thismd.equals("null"))) {
                            val file1 = File(element.name)
                            file1.delete()

                            textsaved += "\n\n\n删除:" + element.name
                            textsaved +=  element.name
                            val msg8 = Message()
                            myHandler1.sendMessage(msg8)


                            FoneActivity.audiolistonserver.remove(element.name)
                            FoneActivity.sContext.sendBroadcast(
                                Intent(
                                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                                    Uri.parse("file://" + element.name)
                                )
                            );


                        }

                    }
                }
            }
        }
    }


    override fun onStop() {
        super.onStop()
        Log.e("FoneActiv","onstop")
    }



    companion object {
        fun createIntent(context: Context): Intent = Intent(context, Fone2Activity::class.java)

    }
}
data class item(
    val name:String,
    val md:String,
    val len:Long
)

data class item2(
    val name:String,
    val md:String
)