package test.navigation.networking.database

import android.util.Log
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import test.navigation.model.dict.*
import test.navigation.store.Account
import test.navigation.store.Account.countList
import test.navigation.ui.utils.ParseStringToList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DatabaseAPI {
    private val database = Firebase.database.reference
    private var pathRoot = "USERS/${Account.USER_ID}"
    private var pathUsername = "$pathRoot/username"
    private var pathWordList = "$pathRoot/wordList"
    private var pathUserPool = "$pathRoot/userPool"
    private var pathResultQA = "$pathRoot/resultQA"

    fun getUserName(){
        database.child(pathUsername).get().addOnSuccessListener { dataSnapshot ->
            Log.i("getData", "Got value ${dataSnapshot.value}")
            if(dataSnapshot.value != null) {
                Account.USER_NAME = dataSnapshot.value.toString()
            }
        }.addOnFailureListener{
            Account.USER_NAME = ""
            Log.e("firebase_welcome", "Error getting data", it)
        }
    }
    fun getWordList(){
        database.child(pathUserPool).get().addOnSuccessListener { dataSnapshot ->
            Log.i("getData", "Got value ${dataSnapshot.value}")
            if(dataSnapshot.value != null) {
                if (!Account.userpool.contains(dataSnapshot.value.toString())) {
                    Account.userpool = dataSnapshot.value.toString()
                    val list = ParseStringToList.from(Account.userpool)
                    list.forEach { loadWord(it) }
                }
            }
        }.addOnFailureListener{
            Account.userpool = ""
            Log.e("firebase_welcome", "Error getting data", it)
        }
    }

    fun addUserPool(word: Word): Boolean{
        return if (Account.userpool.contains(word.word)){
            false
        } else {
            val value =
                    if (Account.userpool == "") Account.userpool + word.word else Account.userpool + "," + word.word
            database.child(pathUserPool).setValue(value)
            Account.userpool = value
            Account.wordList?.add(word)
            writeNewWord(word)
            true
        }
    }

    fun getCountList(){
        database.child(pathResultQA).child("countList").get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.value != null) {
                countList = dataSnapshot.value.toString()
            }
        }.addOnFailureListener{
            countList = ""
            Log.e("getCountTest", "Error getting data", it)
        }
    }

    fun storeResult(numQuest: Int, numCorrect: Int){
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val value =
                if (countList == "") countList + currentDateTime else "$countList,$currentDateTime"
        countList = value
        val resultUpdate = hashMapOf(
                "/$pathResultQA/countList"                   to value,
                "/$pathResultQA/countTest"                   to ServerValue.increment(1),
                "/$pathResultQA/$currentDateTime/numQuest"   to numQuest,
                "/$pathResultQA/$currentDateTime/numCorrect" to numCorrect,
        )
        database.updateChildren(resultUpdate)
        Log.e("storeResult", "store successfully")
    }


    fun loadResult(){
        ParseStringToList.from(countList).forEach { currentDateTime ->
            database.child(pathResultQA).child(currentDateTime).get().addOnSuccessListener { dataSnapshot ->
                if(dataSnapshot.value != null) {
                    Account.RESULT.add(hashMapOf(
                            "numQuest"          to dataSnapshot.child("numQuest").value!!.toString(),
                            "numCorrect"        to dataSnapshot.child("numCorrect").value!!.toString(),
                    ))
                }
            }.addOnFailureListener{
                Account.RESULT.add(hashMapOf(
                        "numQuest"          to "",
                        "numCorrect"        to "",
                ))
                Log.e("loadResult", "Error getting data", it)
            }
        }
    }

    private fun writeNewWord(word: Word) {
        val postValues = word.toMap()
        val childUpdates = hashMapOf<String, Any>(
                "/$pathWordList/${word.word}" to postValues
        )
        database.updateChildren(childUpdates)
    }
    fun clicked(word: String) {
        val updates: MutableMap<String, Any> = HashMap()
        updates["/$pathWordList/$word/isFavorite"] = true
        database.updateChildren(updates)
    }
    fun unClicked(word: String) {
        val updates: MutableMap<String, Any> = HashMap()
        updates["/$pathWordList/$word/isFavorite"] = false
        database.updateChildren(updates)
    }
    private fun loadWord(key: String) {
        database.child(pathWordList).get().addOnSuccessListener { dataSnapshot ->
            Log.e("loadWordList", "Got key $key")
            Log.e("loadWordList", "Got child ${dataSnapshot.child(key)}")
            val path        = dataSnapshot.child(key)
            val word        = path.child("word").value.toString()
            val phonetics   = mutableListOf<Phonetic>()
            val meanings    = mutableListOf<Meaning>()
            val isFavorite  = path.child("isFavorite").value.toString() == "true"
            path.child("phonetics").apply {
                var i = 0
                while(this.hasChild("$i")){
                    phonetics.add(
                            Phonetic(
                                    this.child("$i/text").value.toString(),
                                    this.child("$i/audio").value.toString(),
                            )
                    )
                    i++
                }
                Log.e("loadWordList", "Got phonetics $phonetics")
            }
            path.child("meanings").apply {
                var i = 0
                while(this.hasChild("$i")){
                    var j = 0
                    val pathDef = this.child("$i/definitions")
                    val definitions= mutableListOf<Definition>()
                    while(pathDef.hasChild("$j")){
                        var z = 0
                        val synonyms= mutableListOf<String>()
                        while(pathDef.child("$j").hasChild("$z")){
                            synonyms.add(pathDef.child("$j/$z").value.toString())
                            z++
                        }
                        definitions.add(
                                Definition(
                                        pathDef.child("$j/definition").value.toString(),
                                        synonyms,
                                        pathDef.child("$j/example").value.toString()
                                )
                        )
                        j++
                    }
                    meanings.add(
                            Meaning(
                                    this.child("$i").child("partOfSpeech").value.toString(),
                                    definitions
                            )
                    )
                    i++
                }
                Log.e("loadWordList", "Got meanings $meanings")
            }
            Account.wordList?.add(Word(word, phonetics, meanings, isFavorite))
            Log.e("loadWordList", "Got word ${Word(word, phonetics, meanings, isFavorite)}")
        }.addOnFailureListener {
            Log.e("firebase_welcome", "Error getting data", it)
        }
    }
}