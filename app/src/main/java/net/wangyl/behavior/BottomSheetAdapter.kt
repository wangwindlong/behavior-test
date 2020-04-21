package net.wangyl.behavior

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.wangyl.behavior.databinding.ItemLayoutBinding

open class BottomSheetAdapter(private val dataList: List<String>?) :
    RecyclerView.Adapter<SessionDetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SessionDetailViewHolder.SessionInfoViewHolder(
            ItemLayoutBinding.inflate(inflater, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_layout
    }

    override fun onBindViewHolder(holder: SessionDetailViewHolder, position: Int) {
        when (holder) {
            is SessionDetailViewHolder.SessionInfoViewHolder -> holder.binding.apply {
                text = "item" + (position + 1)
                color = Color.parseColor("#33FF0000")
//                tagViewPool = tagRecycledViewPool
//                lifecycleOwner = adapterLifecycleOwner
                executePendingBindings()
            }
        }
    }
}
