package test.navigation.ui.fragment.print

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import test.navigation.networking.database.DatabaseAPI
import test.navigation.model.dict.Word
import test.navigation.ui.utils.PlayAudioManager

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
    var favListener: PrintAdapterListener? = null

    interface PrintAdapterListener {
        fun onItemClicked(word: Word)
    }

    override fun submitList(list: List<Word>?) {
        super.submitList(list)
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent) as ViewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, listener!!, favListener!!)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvWord: TextView? = itemView.findViewById(R.id.tv_word_view)
        private val tvMeaning: TextView? = itemView.findViewById(R.id.tv_meaning)
        private val tvPhonetic: TextView? = itemView.findViewById(R.id.tv_phonetic)
        private val tvPartOfSpeech: TextView? = itemView.findViewById(R.id.tv_part_of_speech_print)
        private val tvExample: TextView? = itemView.findViewById(R.id.tv_example_print)
        private val heart = itemView.findViewById<ToggleButton>(R.id.heart)
        private val btSound = itemView.findViewById<Button>(R.id.btn_sound_print)

        companion object {
            // khởi tạo layout cho item_view
            fun from(parent: ViewGroup) : RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.word_view, parent, false)
                return ViewHolder(view)
            }
        }
        @SuppressLint("SetTextI18n")
        fun bind(word: Word, listener: PreCachingLayoutManager.WordItemListener, favListener: PrintAdapterListener) {
            itemView.setOnClickListener {
                listener.onItemClicked(word)
            }
            heart.isChecked = word.isFavorite
            tvWord!!.text = word.word
            tvMeaning!!.text = word.meanings[0].definitions[0].definition
            tvPhonetic!!.text = word.phonetics[0].text
            tvPartOfSpeech!!.text = word.meanings[0].partOfSpeech
            tvExample!!.text = "Example: '" + word.meanings[0].definitions[0].example + "'"
            heart.setOnCheckedChangeListener { _, _ ->
                favListener.onItemClicked(word)
            }
            btSound.setOnClickListener {
                PlayAudioManager.prepareAudioFromUrl(itemView.context, word.phonetics[0].audio)
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
