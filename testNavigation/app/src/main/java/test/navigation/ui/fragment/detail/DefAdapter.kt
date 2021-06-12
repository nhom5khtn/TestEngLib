package test.navigation.ui.fragment.detail

import android.util.Log
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

        fun bindViewPartOfSpeech(pos: Int) {
            val mPartOfSpeech: TextView = itemView.findViewById(R.id.tv_part_of_speech_view)
            val index = findIndex(pos).toMutableList()
            index[1] = 0
            val partOfSpeech = meanings!![index[0]].partOfSpeech
            mPartOfSpeech.text = partOfSpeech
            Log.e("bindViewPartOfSpeech ", "[$pos,$partOfSpeech]")
            Log.e("index ", "[${index[0]},${index[1]}]")
        }

        fun bindViewDefinition(pos: Int) {
            val mDefinition: TextView = itemView.findViewById(R.id.tv_def)
            val mExample: TextView = itemView.findViewById(R.id.tv_example)
            val mSynonyms: TextView = itemView.findViewById(R.id.tv_synonyms)

            val index = findIndex(pos)
            val definition = meanings!![index[0]].definitions[index[1]].definition
            val example = meanings!![index[0]].definitions[index[1]].example
            val synonyms =  meanings!![index[0]].definitions[index[1]].synonyms
            mDefinition.text = definition
            mExample.text = example
            mSynonyms.text = synonyms.toString()
            Log.e("bindViewDefinition ", "[$pos,$definition]")
            Log.e("index ", "[${index[0]},${index[1]}]")
        }
    }

    inner class PartOfSpeechViewHolder(itemView: View) : ViewHolder(itemView)

    inner class DefinitionsItemViewHolder(itemView: View) : ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val v: View
        Log.e("onCreateViewHolder ", "[$viewType]")
        return when (viewType) {
            PART_OF_SPEECH_VIEW -> {
                Log.e("PART_OF_SPEECH_VIEW ", "[$viewType]")
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.part_of_speech_view, parent, false)
                PartOfSpeechViewHolder(v)
            }
            else -> {
                // DEFINITION_VIEW
                Log.e("DEFINITION_VIEW ", "[$viewType]")
                v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.def_view, parent, false)
                DefinitionsItemViewHolder(v)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            Log.e("onBindViewHolder ", "[$position]")
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
        var size: Int = meanings?.size!!
        meanings?.forEach { meaning -> size += meaning.definitions.size }
        return size
    }

    override fun getItemViewType(position: Int): Int {
        if (meanings == null) return super.getItemViewType(position)
        if (itemCount > 0) {
            var ref = 0
            meanings!!.forEach{ meaning ->
                if(ref == position) return PART_OF_SPEECH_VIEW
                else{
                    ref += meaning.definitions.size.inc()
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
        var currentCount = meanings?.get(0)?.definitions?.size!!
        var posStart = 0
        var posEnd = meanings?.get(0)?.definitions?.size!!
        for (i in 1 until meanings?.size!!){
            if(position in posStart..posEnd) return listOf(i-1, position - posStart.inc())
            //i-1: index of meanings, position.inc() - posStart: index of defs
            else{
                currentCount += meanings?.get(i)?.definitions?.size?.inc()!!
                posStart = posEnd.inc()
                posEnd = currentCount
            }
        }
        return listOf(-1, -1)
    }
}