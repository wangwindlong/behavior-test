package net.wangyl.behavior

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import net.wangyl.behavior.databinding.ItemLayoutBinding

class TypeAdapter internal constructor(
    private val context: Context?,
    private val datas: List<String?>?,
    private val isOpenLoadMore: Boolean
) : RecyclerView.Adapter<SessionDetailViewHolder>() {
    private val c: IntArray

    //    @Override
    //    protected void convert(RecyclerView.ViewHolder holder, String data, int position) {
    //        holder.setBgColor(R.id.item_tv, c[position % 3]);
    //        holder.setText(R.id.item_tv, "item" + (position + 1));
    //    }
    //
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_layout -> SessionDetailViewHolder.SessionInfoViewHolder(
                ItemLayoutBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.item_layout
    }

    override fun onBindViewHolder(holder: SessionDetailViewHolder, position: Int) {
        when (holder) {
            is SessionDetailViewHolder.SessionInfoViewHolder -> holder.binding.apply {
                text = "item" + (position + 1)
                color = c[position % 3]
//                tagViewPool = tagRecycledViewPool
//                lifecycleOwner = adapterLifecycleOwner
                executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return datas?.size ?: 0
    }

    init {
        c = intArrayOf(
            Color.parseColor("#33FF0000"),
            Color.parseColor("#3300FF00"),
            Color.parseColor("#330000FF")
        )
    }
}

sealed class SessionDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class SessionInfoViewHolder(
        val binding: ItemLayoutBinding
    ) : SessionDetailViewHolder(binding.root)

//    class SpeakerViewHolder(
//        val binding: ItemSpeakerBinding
//    ) : SessionDetailViewHolder(binding.root)
//
//    class RelatedViewHolder(
//        val binding: ItemSessionBinding
//    ) : SessionDetailViewHolder(binding.root)

    class HeaderViewHolder(
        itemView: View
    ) : SessionDetailViewHolder(itemView)
}