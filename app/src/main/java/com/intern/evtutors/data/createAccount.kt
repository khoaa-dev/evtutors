package com.intern.evtutors.data

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.intern.evtutors.models.Lesson
import com.intern.evtutors.models.account
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject

class createAccount(private val dispatcher: CoroutineDispatcher) {
    suspend fun create_account(Account: account): Any =
        withContext(dispatcher){

            var bodyObject: JSONObject = JSONObject()
            try {
                bodyObject.put("", Account.userName)
                bodyObject.put("", Account.passWord)
                bodyObject.put("", Account.who)

                val request = AndroidNetworking.put("").addJSONObjectBody(bodyObject).build()
                request.executeForJSONObject()
            } catch(e:Exception) {
                Log.d("Create account  error: ", e.toString())
            }
        }



}