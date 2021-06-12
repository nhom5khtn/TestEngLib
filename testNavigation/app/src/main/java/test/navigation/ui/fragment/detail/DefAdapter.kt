package test.navigation.ui.fragment.detail

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import test.navigation.model.dict.Meaning


class DefAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var meanings: List<Meaning>? = ArrayList()

    fun setList(firstList: List<Meaning>) {
        this.meanings = firstList as ArrayList
    }

    open inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mPartOfSpeech: TextView = itemView.findViewById(R.id.tv_word_key)
        private val mDefinition: TextView = itemView.findViewById(R.id.tv_def)
        private val mExample: TextView = itemView.findViewById(R.id.tv_example)
        private val mSynonyms: TextView = itemView.findViewById(R.id.tv_synonyms)

        fun bindViewPartOfSpeech(pos: Int) {
            val index = findIndex(pos)
            val partOfSpeech = meanings!![index[0]].partOfSpeech
            mPartOfSpeech.text = partOfSpeech
        }

        fun bindViewDefinition(pos: Int) {
            var index = findIndex(pos)
            val definition = meanings!![index[0]].definitions[index[1]].definition
            val example = meanings!![index[0]].definitions[index[1]].example
            val synonyms =  meanings!![index[0]].definitions[index[1]].synonyms
            mDefinition.text = definition
            mExample.text = example
            mSynonyms.text = synonyms.toString()
        }
    }

    inner class PartOfSpeechViewHolder(itemView: View) : ViewHolder(itemView)

    inner class DefinitionsItemViewHolder(itemView: View) : ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        return when (viewType) {
            PART_OF_SPEECH_VIEW -> {
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.part_of_speech_view, parent, false)
                PartOfSpeechViewHolder(v)
            }
            else -> {
                // DEFINITION_VIEW
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.def_view, parent, false)
                DefinitionsItemViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            when (holder) {
                is DefinitionsItemViewHolder -> {
                    holder.bindViewDefinition(position)
                }
                is PartOfSpeechViewHolder -> {
                    holder.bindViewPartOfSpeech(position)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        var size = meanings!!.size
        for (i in meanings!!.indices)
            size += meanings?.get(i)?.definitions?.size!!
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (meanings == null) return super.getItemViewType(position)
        val defSizes = mutableListOf<Int>()
        val meanSize = meanings!!.size
        for (i in meanings!!.indices)
            defSizes[i] =  meanings?.get(i)?.definitions?.size!!

        if (itemCount > 0) {
            var ref = 0
            for(i in 0 until meanSize){
                if(ref == position) return PART_OF_SPEECH_VIEW
                else{
                    ref+=1
                    ref+=defSizes[i]
                }
            }
            return DEFINITION_VIEW
        }
        return super.getItemViewType(position)
    }

    companion object {
        private const val PART_OF_SPEECH_VIEW = 1
        private const val DEFINITION_VIEW = 2
    }

    fun findIndex(position: Int): List<Int> {
        val defSizes = mutableListOf<Int>()
        val meanSize = meanings!!.size
        for (i in meanings!!.indices)
            defSizes[i] =  meanings?.get(i)?.definitions?.size!!
        var currentCount = defSizes[0]
        var posStart = 0
        var posEnd = defSizes[0]
        for (i in 1 until meanSize){
            if(position in posStart..posEnd) return listOf(i-1, posStart)
            //i-1: order of array, posStart: position of header
            else{
                currentCount+=1
                currentCount+=defSizes[i]
                posStart = posEnd + 1
                posEnd = currentCount
            }
        }
        return listOf(-1, -1)
    }
}