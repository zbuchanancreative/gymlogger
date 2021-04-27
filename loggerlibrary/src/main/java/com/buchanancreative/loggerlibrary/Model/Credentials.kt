package com.buchanancreative.loggerlibrary.Model

/**
 * Created by buchanancreative on 8/4/18.
 */
class Credentials(private val password: String, private val username: String) {
    fun getUsername(): String {
        return username
    }

    fun getPassword(): String {
        return password
    }
}
