package test.navigation.model.dict

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import test.navigation.R
import java.lang.reflect.Array.set

class WordAdapter : RecyclerView.Adapter<WordAdapter.ViewHolder>(){
    var data: List<Word> = listOf()
        set(value){
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        var view = layoutInflater.inflate(R.layout.word_view, parent, false)
        return ViewHolder(view)
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var item =data[position]
        holder.tv_word.text =item.word
        holder.tv_meaning.text = item.meanings
        holder.tv_movie_overview = item.phonetics
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return data.size
        TODO("Not yet implemented")
    }

    class ViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView)
        val tv_word = itemView.findViewById<TextView>(R.id.tv_word)
        val tv_meaning = itemView.findViewById<TextView>(R.id.tv_meaning)
        val tv_movie_overview = itemView.findViewById<TextView>(R.id.tv_movie_overview)
}