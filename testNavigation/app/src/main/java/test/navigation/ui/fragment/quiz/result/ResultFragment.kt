package test.navigation.ui.fragment.quiz.result

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_result.*
import test.navigation.R
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account
import test.navigation.ui.utils.PlayAudioManager

class ResultFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_score.text = "Your score is ${Account.CORRECT_ANSWERS} out of ${Account.TOTAL_QUESTIONS}"

        if(Account.CORRECT_ANSWERS < Account.TOTAL_QUESTIONS/2 || Account.CORRECT_ANSWERS == 0) {
            PlayAudioManager.playAudioFromRaw(context, "fail")
            iv_result.setImageResource(R.drawable.failure)
            tv_resultString.text = resources.getStringArray(R.array.string_array_result)[0]
        } else {
            PlayAudioManager.playAudioFromRaw(context, "congrat")
            iv_result.setImageResource(R.drawable.trophy)
            tv_resultString.text = resources.getStringArray(R.array.string_array_result)[1]
        }
        btn_finish.setOnClickListener {
            PlayAudioManager.stopAudio()
            findNavController().navigate(R.id.action_resultFragment_to_homeFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.e("CountTest ", Account.countList)
        DatabaseAPI.storeResult(Account.TOTAL_QUESTIONS,Account.CORRECT_ANSWERS)
    }
}