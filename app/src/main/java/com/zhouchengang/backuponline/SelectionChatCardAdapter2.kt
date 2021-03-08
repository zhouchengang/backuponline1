package com.zhouchengang.backuponline


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.zhouchengang.backuponline.UploadUtil.uploadFile
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_chatcard_left.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


class SelectionChatCardAdapter2(val itemMapin: HashMap<String,Int> )
    : RecyclerView.Adapter<SelectionChatCardAdapter2.ChatCardViewHolder>() {

    var itemList :MutableList <SelectionChatCardItem> =ArrayList()



    init {
        for ((index, element) in itemMapin) {
            if (element == 4 || element == 5)
                itemList.add(
                    SelectionChatCardItem(
                        UploadUtil.pathtoname(index),
                        UploadUtil.pathtodir(index),
                        element,
                        ""
                        //"" + UploadUtil.getFileMD5s(File(index), 16)
                    )
                )
        }

        for ((index, element) in itemMapin) {
            if (element == 2)
                itemList.add(
                    SelectionChatCardItem(
                        UploadUtil.pathtoname(index),
                        UploadUtil.pathtodir(index),
                        element,
                        ""
                        //"" + UploadUtil.getFileMD5s(File(index), 16)
                    )
                )
        }

        itemList.sortBy { ""+(10-it.statem)+it.path2 }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_chatcard_left, parent, false)
        return ChatCardViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = itemList.size


    override fun onBindViewHolder(holder: ChatCardViewHolder, position: Int) {

        val item = itemList[position]
        holder.setSelectionChatCardItem(item)
        holder.setOnClickCardListener {
            notifyItemChanged(position)
        }
    }

    class ChatCardViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {

        lateinit var itemsaved: SelectionChatCardItem

        fun setSelectionChatCardItem(item: SelectionChatCardItem) {
            itemsaved = item;

            containerView.customdir.text=itemsaved.path2+"\n"+itemsaved.md
            //containerView.customdir.text=itemsaved.path2+"\n"+itemsaved.md

            var myHandler1:Handler = object : Handler() {
                override fun handleMessage(msg: Message) {
                    containerView.customdir.text=itemsaved.path2+"\n"+itemsaved.md
                }
            }

            runBlocking {
                GlobalScope.launch {
                    println(itemsaved.path2 + itemsaved.filename +"    "+ itemsaved.md)
                    if(itemsaved.md.isEmpty()){
                        itemsaved.md =
                            UploadUtil.getFileMD5s(
                                File(itemsaved.path2 + itemsaved.filename),
                                16
                            )
                        println(itemsaved.path2 + itemsaved.filename +"    "+ itemsaved.md)
                        val msg = Message()
                        myHandler1.sendMessage(msg)
                    }
                }
            }

            if(item.statem==5 ){
                containerView.customname.text=itemsaved.filename +"\n(未上传...)"
                containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                //containerView.iconImageView.setImageResource(R.drawable.ic_android)
            }
            if(item.statem==4 ){
                containerView.customname.text=itemsaved.filename +"\n(上传中...)"
                containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                //containerView.iconImageView.setImageResource(R.drawable.ic_info)
            }
            if(item.statem==2 ){
                containerView.customname.text=itemsaved.filename +"\n(已上传...)"
                containerView.bg.setBackground(ColorDrawable(Color.parseColor("#BBBBBB")))
                //containerView.iconImageView.setImageResource(R.drawable.ic_attention)
            }

            containerView.messageCard.setOnClickListener {
                Log.e("S","clicked");
                if(item.statem == 5){
                    var myHandler:Handler = object : Handler() {
                        override fun handleMessage(msg: Message) {
                            containerView.customname.text=itemsaved.filename +"\n(已上传...)"
                            containerView.bg.setBackground(ColorDrawable(Color.parseColor("#BBBBBB")))
                            //containerView.iconImageView.setImageResource(R.drawable.ic_attention)
                        }
                    }

                    if(FoneActivity.sureupload){
                        containerView.customname.text =  itemsaved.filename +"\n(上传中...)"
                        containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                        item.statem =4;

                        if(FoneActivity.audiolistonserver.contains(item.path2+item.filename)){
                            FoneActivity.audiolistonserver.put(item.path2+item.filename, 4);
                        }

                        runBlocking{
                            GlobalScope.launch {
                                uploadFile(""+item.path2+item.filename)
                                item.statem =2;
                                if(FoneActivity.audiolistonserver.contains(item.path2+item.filename)){
                                    FoneActivity.audiolistonserver.put(item.path2+item.filename, 2);
                                }
                                val msg = Message()
                                myHandler.sendMessage(msg)
                            }
                        }
                    }
                }


                if(FoneActivity.sureupload2) {
                    val mIntent = Intent("ACTION_WEBRENEW")
                    mIntent.putExtra("yaner", "file://" + item.path2 + item.filename)
                    FoneActivity.sContext.sendBroadcast(mIntent)
                }

            }
        }

        fun setOnClickCardListener(listener: (view: View) -> Unit) {

        }
    }

    data class SelectionChatCardItem(
        var filename: String,
        var path2: String,
        var statem :Int,
        var md:String

        //0表示无状态
        //1表示下载中
    ) {
    }
}
