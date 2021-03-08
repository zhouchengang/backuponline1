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
import com.zhouchengang.backuponline.UploadUtil.downloadFile
import com.zhouchengang.fileonlinelaunchapp.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.view_chatcard_left.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.File


class SelectionChatCardAdapter(val itemMapin: HashMap<String,Int> )
    : RecyclerView.Adapter<SelectionChatCardAdapter.ChatCardViewHolder>() {

    var itemList :MutableList <SelectionChatCardItem> =ArrayList()


    init {
        for((index,element) in itemMapin){
            if(element==0 ||element==1)
                itemList.add(
                    SelectionChatCardItem(
                        UploadUtil.pathtoname(index),
                        UploadUtil.pathtodir(index),
                        element
                    )
                )
        }

        for((index,element) in itemMapin){
            if(element==2)
                itemList.add(
                    SelectionChatCardItem(
                        UploadUtil.pathtoname(index),
                        UploadUtil.pathtodir(index),
                        element
                    )
                )
        }

        //itemList.sortBy {it.filename }

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

            containerView.customdir.text=itemsaved.path2
            if(item.statem==0 ){
                containerView.customname.text=itemsaved.filename +"\n(在云端...)"
                containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                //containerView.iconImageView.setImageResource(R.drawable.ic_android)
            }
            if(item.statem==1 ){
                containerView.customname.text=itemsaved.filename +"\n(下载中...)"
                containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                //containerView.iconImageView.setImageResource(R.drawable.ic_info)
            }
            if(item.statem==2 ){
                containerView.customname.text=itemsaved.filename +"\n(下载完成...)"
                containerView.bg.setBackgroundColor(Color.parseColor("#BBBBBB"))

                //containerView.iconImageView.setImageResource(R.drawable.ic_attention)
            }

            containerView.messageCard.setOnClickListener {
                Log.e("S","clicked");
                if(item.statem == 0){


                    var myHandler:Handler = object : Handler() {
                        override fun handleMessage(msg: Message) {
                            containerView.customname.text=itemsaved.filename +"\n(下载完成...)"
                            containerView.bg.setBackgroundColor(Color.parseColor("#BBBBBB"))
                            //containerView.iconImageView.setImageResource(R.drawable.ic_attention)
                        }
                    }

                    if(FoneActivity.suredownload){
                        containerView.customname.text =  itemsaved.filename +"\n(下载中...)"
                        containerView.bg.setBackground(ColorDrawable(Color.parseColor("#FFFFFF")))
                        item.statem =1;


                        if(FoneActivity.audiolistonserver.contains(item.path2+item.filename)){
                            FoneActivity.audiolistonserver.put(item.path2+item.filename, 1)
                        }

                        runBlocking{
                            GlobalScope.launch {
                                downloadFile(item.path2+item.filename)
                                Log.e("Main", item.filename+"下载完成")
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


                if(FoneActivity.suredownload2) {
                    try {
                        val f = File(item.path2 + item.filename)
                        Log.e("sel", item.path2 + item.filename)
                        if (f.exists()) {
                            val mIntent = Intent("ACTION_WEBRENEW")
                            mIntent.putExtra("yaner", "file://" + item.path2 + item.filename)
                            FoneActivity.sContext.sendBroadcast(mIntent)
                        } else {
                            val mIntent = Intent("ACTION_WEBRENEW")
                            mIntent.putExtra(
                                "yaner",
                                "http://"+FoneActivity.read.getString("ip", "")+":8888/static/videos/" + item.path2 + item.filename
                            )
                            FoneActivity.sContext.sendBroadcast(mIntent)
                        }
                    } catch (e: Exception) {
                    }
                }


            }
        }

        fun setOnClickCardListener(listener: (view: View) -> Unit) {

        }
    }

    data class SelectionChatCardItem(
        var filename: String,
        var path2: String,
        var statem :Int

        //0表示无状态
        //1表示下载中
    ) {
    }
}
