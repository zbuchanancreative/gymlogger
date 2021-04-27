package com.buchanancreative.gymlogger.View

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.buchanancreative.gymlogger.LoggerApp
import com.buchanancreative.gymlogger.R
import com.buchanancreative.loggerlibrary.Interface.LoginListener
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

import kotlinx.android.synthetic.main.account_activity.*

import android.net.Uri
import androidx.appcompat.app.AlertDialog


class AccountActivity : AppCompatActivity(), LoginListener {

    private val RC_SIGN_IN = 1

    private lateinit var auth: FirebaseAuth


    companion object {
        val hasAcceptedPrivacyPolicyKey = "acceptedPrivacyPolicy"
    }

    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.account_activity)

        signIn.setOnClickListener {
            startSignInActivity()
        }

        privacyPolicy.setOnClickListener {
            openPrivacyPolicyWebsite()
        }

        syncWithWear.setOnClickListener {
            testDataTransferToWear()
        }
        auth = FirebaseAuth.getInstance()

    }

    private fun openPrivacyPolicyWebsite() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sites.google.com/view/fortis-lift-privacy-policy"))
        startActivity(browserIntent)
    }


    override fun onResume() {
        super.onResume()

        if(!hasAcceptedPrivacyPolicy()) {
            showPrivacyPolicy()
        }

        if (isSignedIn()) {
            setupUserIsSignedIn()
        } else {
            setupUserIsNotSignedIn()
        }

        auth.addAuthStateListener {
            if (it.currentUser != null) {
                setupUserIsSignedIn()
            } else {
                setupUserIsNotSignedIn()
            }
        }
    }

    private fun hasAcceptedPrivacyPolicy(): Boolean {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getBoolean(hasAcceptedPrivacyPolicyKey, false)
    }

    private fun showPrivacyPolicy() {
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.do_you_agree_to_privacy_policy))
                    .setPositiveButton(getString(R.string.i_agree)) { _, _ ->
                        userAcceptedPrivacyPolicy()
                    }.setNeutralButton(getString(R.string.open_policy)) { _, _ ->
                        openPrivacyPolicyWebsite()
                    }.setOnCancelListener { finish() }
            builder.create().show()

    }

    private fun userAcceptedPrivacyPolicy() {
        val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putBoolean(hasAcceptedPrivacyPolicyKey, true)
            commit()
        }
    }

    private fun setupUserIsSignedIn() {
        signIn.visibility = View.GONE
        signOut.visibility = View.VISIBLE
        userInfo.visibility = View.VISIBLE
        userInfo.text = getString(R.string.user_info,  auth.currentUser?.email)
        signOut.setOnClickListener { signOut() }
    }

    private fun setupUserIsNotSignedIn() {
        signIn.visibility = View.VISIBLE
        signOut.visibility = View.GONE
        userInfo.visibility = View.GONE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Log.i("MAIN", "sign in succeeded: " + auth.currentUser.toString())

            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                Log.i("MAIN", "sign in failed")
            }
        }
    }

    private fun testDataTransferToWear() {
        val dataMap = PutDataMapRequest.create("/user")
        dataMap.getDataMap().putString("userId", (application as LoggerApp).getUserId())
        val request = dataMap.asPutDataRequest()
        request.setUrgent()

        val ctx = applicationContext ?: return
        val dataItemTask = Wearable.getDataClient(ctx).putDataItem(request)
        dataItemTask.addOnSuccessListener { dataItem -> Log.i("TESTING", "Sending image was successful: $dataItem") }
    }



    override fun startSignInActivity() {
        signInUser()
    }

    fun signInUser() {
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun signOut() {
        auth.signOut()
    }

    override fun isSignedIn(): Boolean {
        return auth.currentUser != null
    }
}