package com.example.tvapplicationpaging3.paging

//import android.view.ViewGroup
//import androidx.emoji2.emojipicker.ItemViewData
//import androidx.paging.PagingDataAdapter
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.RecyclerView
//
//class ItemsAdapter(
//    diffCallback: DiffUtil.ItemCallback<ItemViewData>
//) : PagingDataAdapter<ItemViewData, ItemsAdapter.ViewHolder>(diffCallback) {
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        return ViewHolder(ViewItemBinding.inflate(LayoutInflater.from(parent.context)), listener)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = getItem(position)
//        holder.setData(item, position)
//    }
//
//    class ViewHolder(
//        //...
//    ) : RecyclerView.ViewHolder(binding.root) {
//        fun setData(data: ItemViewData?, index: Int) {
//            //...
//        }
//    }
//}
//
//// Paging 3ライブラリが、Adapter内の商品のチェックを行うための処理
//object ItemComposer : DiffUtil.ItemCallback<ItemViewData>() {
//    override fun areItemsTheSame(oldItem: ItemViewData, newItem: ItemViewData): Boolean {
//        return oldItem.id == newItem.id
//    }
//
//    override fun areContentsTheSame(oldItem: ItemViewData, newItem: ItemViewData): Boolean {
//        return oldItem == newItem
//    }
//}