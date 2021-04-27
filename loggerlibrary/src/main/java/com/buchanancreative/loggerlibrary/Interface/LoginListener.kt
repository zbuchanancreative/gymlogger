package com.buchanancreative.loggerlibrary.Interface

interface LoginListener {
    fun startSignInActivity()
    fun signOut()
    fun isSignedIn(): Boolean
}