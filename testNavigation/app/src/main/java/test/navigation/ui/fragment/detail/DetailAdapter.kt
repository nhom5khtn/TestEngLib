package test.navigation.ui.fragment.detail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import test.navigation.model.dict.Meaning


class DetailAdapter :
    ListAdapter<Meaning, DetailAdapter.ViewHolder>(DetailDiffUtilCallback()) {
    class DetailDiffUtilCallback : DiffUtil.ItemCallback<Meaning>() {
        override fun areItemsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
            return oldItem.partOfSpeech == newItem.partOfSpeech
        }

        override fun areContentsTheSame(oldItem: Meaning, newItem: Meaning): Boolean {
            return oldItem == newItem
        }
    }
    var listener: PreCachingLayoutManager.MeaningItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent) as ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun from(parent: ViewGroup) : RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.meaning_view, parent, false)
                return ViewHolder(view)
            }
        }
        fun bind(word: Meaning, listener: PreCachingLayoutManager.MeaningItemListener) {
            itemView.setOnClickListener {
                listener.onItemClicked(word)
            }
        }
    }
    class PreCachingLayoutManager : LinearLayoutManager {
        private val defaultExtraLayoutSpace = 600
        private var extraLayoutSpace = -1
        private var context: Context? = null

        constructor(context: Context?) : super(context) {
            this.context = context
        }

        constructor(context: Context, extraLayoutSpace: Int) : super(context) {
            this.context = context
            this.extraLayoutSpace = extraLayoutSpace
        }

        constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(context, orientation, reverseLayout) {
            this.context = context
        }

        fun setExtraLayoutSpace(extraLayoutSpace: Int) {
            this.extraLayoutSpace = extraLayoutSpace
        }

        override fun getExtraLayoutSpace(state: RecyclerView.State?): Int {
            return if (extraLayoutSpace > 0) {
                extraLayoutSpace
            } else defaultExtraLayoutSpace

        }

        interface MeaningItemListener {
            fun onItemClicked(word: Meaning)
        }
    }
}
