package com.buchanancreative.loggerlibrary.Interface

/**
 * Created by buchanancreative on 7/7/18.
 */

interface AsyncResponseDelegate {
    fun processFinished(result: String)
    fun requestDidSucceed()
}
