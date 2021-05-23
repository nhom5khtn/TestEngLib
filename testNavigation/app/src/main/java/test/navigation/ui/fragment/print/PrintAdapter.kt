package test.navigation.ui.fragment.print

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import test.navigation.model.dict.Word

class PrintAdapter :
    ListAdapter<Word, PrintAdapter.ViewHolder>(PrintDiffUtilCallback()) {
    var listener: MovieItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.word_view, parent, false)
        return ViewHolder(view!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener!!)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvWord: TextView? = itemView.findViewById(R.id.tv_word)
        private val tvMeaning: TextView? = itemView.findViewById(R.id.tv_meaning)

        fun bind(word: Word, listener: MovieItemListener) {
            itemView.setOnClickListener {
                listener.onItemClicked(word)
            }
            tvWord!!.text = word.word
            tvMeaning!!.text = word.meanings.toString()
        }
    }
    class PrintDiffUtilCallback : DiffUtil.ItemCallback<Word>() {
        override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
            return oldItem == newItem
        }
    }
    interface MovieItemListener {
        fun onItemClicked(word: Word)
    }
}
