package test.navigation.networking.database

import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import test.navigation.model.dict.ParseWordList
import test.navigation.model.dict.Word
import test.navigation.store.Account

object DatabaseAPI {
    private val database = Firebase.database.reference
    fun getUserName(){
        database.child("USERS")
            .child(Account.USER_ID)
            .child("usename")
            .get().addOnSuccessListener { dataSnapshot ->
                Log.i("getData", "Got value ${dataSnapshot.value}")
                if(dataSnapshot.value != null) {
                    Account.USER_NAME = dataSnapshot.value.toString()
                }
            }.addOnFailureListener{
                Account.USER_NAME = ""
                Log.e("firebase_welcome", "Error getting data", it)
            }
    }
    fun getData(){
        database.child("USERS")
                .child(Account.USER_ID)
                .child("wordList")
                .get().addOnSuccessListener { dataSnapshot ->
                    Log.i("getData", "Got value ${dataSnapshot.value}")
                    if(dataSnapshot.value != null) {
                        Account.userpool = dataSnapshot.value.toString()
                    }
                }.addOnFailureListener{
                    Account.userpool = ""
                    Log.e("firebase_welcome", "Error getting data", it)
                }
        database.child("USERS")
                .child(Account.USER_ID)
                .child("favWordList")
                .get().addOnSuccessListener { dataSnapshot ->
                    Log.i("getData", "Got value ${dataSnapshot.value}")
                    if(dataSnapshot.value != null) {
                        Account.favUserPool = dataSnapshot.value.toString()
                    }
                }.addOnFailureListener{
                    Account.favUserPool = ""
                    Log.e("firebase_welcome", "Error getting data", it)
                }
    }
    fun addUserPool(word: Word): Boolean{
        if (Account.userpool.contains(word.word)){
            return false
        } else {
            val value =
                if (Account.userpool == "") Account.userpool + word.word else Account.userpool + "," + word.word
            database
                .child("USERS")
                .child(Account.USER_ID)
                .child("wordList")
                .setValue(value)
            Account.userpool = value
            Account.wordList?.add(word)
            return true
        }
    }
    fun addFavUserPool(word: Word){
        Account.wordList?.indexOf(word)?.let { Account.wordList?.get(it)?.isFavorite = true}
        val value = if(Account.favUserPool == "") Account.favUserPool + word.word else Account.favUserPool + "," + word.word
        database
                .child("USERS")
                .child(Account.USER_ID)
                .child("favWordList")
                .setValue(value)
        Account.favUserPool = value
    }
    fun removeFavUserPool(word: Word){
        Account.wordList?.indexOf(word)?.let { Account.wordList?.get(it)?.isFavorite = false}
        val value = separate(Account.favUserPool, word.word)
        database
                .child("USERS")
                .child(Account.USER_ID)
                .child("favWordList")
                .setValue(value)
        Account.favUserPool = value
    }
    private fun separate(list: String, key: String): String{
        if(!list.contains(",")){
            return ""
        } else {
            val first 	= list.substring(0,list.indexOf(","))
            println("first = $first")
            val last 	= list.substring(list.lastIndexOf(",")+1)
            println("last = $last")
            return when (key){
                first -> {
                    list.split("${key},")[1]
                }
                last -> {
                    list.split(",${key}")[0]
                }
                else -> {
                    val arr = list.split(",${key},")
                    arr[0] + arr[1]
                }
            }
        }
    }

}