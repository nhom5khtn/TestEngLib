package test.navigation.ui.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_sign_up.*
import test.navigation.R

class SignUpFragment : Fragment() {

    private lateinit var mAuth: FirebaseAuth
    private lateinit var refUsers: DatabaseReference
    private var firebaseUserID:String=""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        btn_signup.setOnClickListener {
            signUpUser()
        }
    }

    private fun signUpUser() {
        val username    = edt_username_signup.text.toString()
        val email       = edt_email_signup.text.toString()
        val password    = edt_password_signup.text.toString()

        when {
            username == "" -> {
                Toast.makeText(activity,"Please write username", Toast.LENGTH_LONG).show()
            }
            email == "" -> {
                Toast.makeText(activity,"Please write email", Toast.LENGTH_LONG).show()
            }
            password == "" -> {
                Toast.makeText(activity,"Please write password", Toast.LENGTH_LONG).show()
            }
            else -> {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        task->
                    if(task.isSuccessful){
                        firebaseUserID = mAuth.currentUser!!.uid
                        refUsers = FirebaseDatabase.getInstance().reference.child("users").child(firebaseUserID)

                        val userHashMap = HashMap<String, Any>()
                        userHashMap["uid"] = firebaseUserID
                        userHashMap["username"]=username

                        refUsers.updateChildren(userHashMap).addOnCompleteListener{
                                task->
                            if(task.isSuccessful){
                                findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
                            }
                        }
                    }
                    else{
                        Toast.makeText(activity,"Error Message: "+ task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}