package com.zhouchengang.backuponline.album

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.zhouchengang.fileonlinelaunchapp.R

/**
 *  @author zhouchengang
 *  @date   2021/2/8
 *  @desc
 */
class PicGridAdapter :
    BaseQuickAdapter<PicStu, BaseViewHolder>(R.layout.item_pic_in_album, null) {

    override fun convert(holder: BaseViewHolder, item: PicStu) {
        holder.setText(R.id.tv_album_name, "" + item.name)
        Glide.with(context)
            .load(item.cover)
            .apply(
                RequestOptions()
                    .transform(GlideRoundTransformCenterCrop(20f))
                    .dontAnimate()
            )
            .into(holder.getView(R.id.iv_album_cover))

        holder.itemView.setOnClickListener { view ->
            PicActivity.launch(context, item.path)
        }
    }
}