package test.navigation.ui.activity.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import test.navigation.R
import test.navigation.networking.database.DatabaseAPI
import test.navigation.store.Account
import test.navigation.ui.activity.main.MainActivity

class SplashActivity : AppCompatActivity() {

    var firebaseUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        if(firebaseUser != null){
            Account.USER_ID = firebaseUser?.uid.toString()
            Account.USER_NAME = "username"
            Account.USER_IMAGE = R.drawable.trophy
            DatabaseAPI.getData()
        }

        var handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000)        // delay 3 seconds to open MainActivity
    }
}