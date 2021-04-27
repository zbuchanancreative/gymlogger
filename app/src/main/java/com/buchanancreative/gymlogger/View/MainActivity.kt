package com.buchanancreative.gymlogger.View

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.buchanancreative.loggerlibrary.Interface.*

import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Util.Constants
import com.buchanancreative.gymlogger.View.LogFragments.*
import com.buchanancreative.gymlogger.View.Stats.LogHistoryListFragment

import com.buchanancreative.loggerlibrary.Model.Exercise
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : AppCompatActivity(), AccountListener {

    var statsFromDate: Date? = null

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private fun isUserSignedIn() = FirebaseAuth.getInstance().currentUser != null


    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)


        val navController = findNavController(nav_host_fragment)
        findViewById<BottomNavigationView>(R.id.bottom_nav)
                .setupWithNavController(navController)
    }

    private fun showSignInDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
                .setPositiveButton("Sign in",
                        DialogInterface.OnClickListener { _, _ ->
                            onAccountClicked()
                        })
                .setNegativeButton(R.string.cancel,null)
        builder.create().show()
    }


    companion object {
        private val TAG = MainActivity::class.java.toString()
    }


    fun showLoginPopupIfNotLoggedIn(message: String) {
        if(!isUserSignedIn()) showSignInDialog(message)
    }


    override fun onAccountClicked() {
        val intent = Intent(baseContext, AccountActivity::class.java)
        startActivity(intent)
    }
}


