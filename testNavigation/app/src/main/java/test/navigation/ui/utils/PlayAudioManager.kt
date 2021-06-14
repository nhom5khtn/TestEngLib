package test.navigation.ui.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import test.navigation.R


object PlayAudioManager {
    private var mediaPlayer: MediaPlayer? = null
    @Throws(Exception::class)
    fun prepareAudioFromUrl(context: Context?, url: String?) {
        mediaPlayer = if (mediaPlayer == null) {
            MediaPlayer.create(context, Uri.parse(url))
        }else {
            killMediaPlayer()
            MediaPlayer.create(context, Uri.parse(url))
        }
        mediaPlayer!!.setOnCompletionListener { killMediaPlayer() }
        mediaPlayer!!.start()
    }
    @Throws(Exception::class)
    fun playAudioFromRaw(context: Context?, select: String?) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context,
            when (select){
                "wrong"     -> R.raw.wrong_answer
                "right"     -> R.raw.correct_answer
                "congrat"   -> R.raw.clap
                "fail"      -> R.raw.fail
                else        -> R.raw.reaching_the_sky // background
            })
        } else {
            killMediaPlayer()
            mediaPlayer = MediaPlayer.create(context,
                    when (select){
                        "wrong"     -> R.raw.wrong_answer
                        "right"     -> R.raw.correct_answer
                        "congrat"   -> R.raw.clap
                        "fail"      -> R.raw.fail
                        else        -> R.raw.reaching_the_sky // background
                    })
        }
        mediaPlayer!!.setOnCompletionListener { killMediaPlayer() }
        mediaPlayer!!.start()
    }
    fun stopAudio(){
        mediaPlayer?.stop()
    }
    private fun killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer!!.reset()
                mediaPlayer!!.release()
                mediaPlayer = null
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}