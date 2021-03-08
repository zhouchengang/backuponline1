package com.zhouchengang.backuponline

import android.content.Intent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhouchengang.fileonlinelaunchapp.R

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
class PicGridAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_home_page_one,null) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_album_name,""+item)
        Glide.with(context)
            .load(item)
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.getView(R.id.iv_album_cover))

        holder.itemView.setOnClickListener { view ->
            context.startActivity(Intent(context,AlbumActivity::class.java))
        }
    }
}