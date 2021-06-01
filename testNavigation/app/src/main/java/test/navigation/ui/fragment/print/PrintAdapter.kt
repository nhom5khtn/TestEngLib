package test.navigation.ui.fragment.print

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import test.navigation.networking.database.DatabaseAPI
import test.navigation.model.dict.Word
import test.navigation.store.Account

class PrintAdapter :
    ListAdapter<Word, PrintAdapter.ViewHolder>(PrintDiffUtilCallback()) {
    class PrintDiffUtilCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }

    var listener: PreCachingLayoutManager.WordItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent) as ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvWord: TextView? = itemView.findViewById(R.id.tv_word)
        private val tvMeaning: TextView? = itemView.findViewById(R.id.tv_meaning)
        val heart = itemView.findViewById<ToggleButton>(R.id.heart)

        companion object {
            // khởi tạo layout cho item_view
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.word_view, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(word: Word, listener: PreCachingLayoutManager.WordItemListener) {
            itemView.setOnClickListener {
                listener.onItemClicked(word)
            }
            heart.isChecked = word.isFavorite
            tvWord!!.text = word.word
            tvMeaning!!.text = word.meanings.toString()
            heart.setOnCheckedChangeListener { _, isChecked ->
                word.isFavorite = isChecked
                if (isChecked) {
                    DatabaseAPI.addFavUserPool(word)
                    Log.e("heart", "stored")
                    Log.e("heart", Account.favUserPool)
                } else {
                    DatabaseAPI.removeFavUserPool(word)
                    Log.e("heart", "not stored")
                    Log.e("heart", Account.favUserPool)
                }
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

        interface WordItemListener {
            fun onItemClicked(word: Word)
        }
    }
}
