package com.intern.evtutors.data

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.intern.evtutors.models.AgoraApp
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject


class CallRepository(private val dispatcher:CoroutineDispatcher) {
    suspend fun getAppInfo():AgoraApp =
        withContext(dispatcher) {
            var appInfo = AgoraApp("", "")
            val request = AndroidNetworking.get("http://call-video-service.herokuapp.com/api/agoraApp")
                .build()
            try {
                val response = request.executeForJSONArray()
                if(response.isSuccess) {
                    val result = response.result as JSONArray
                    appInfo.appId = result.getJSONObject(0).getString("appID")
                    appInfo.appCerti = result.getJSONObject(0).getString("appCertificate")
                    Log.d("App id for token 2: ", result.getJSONObject(0).getString("appID"))
                } else {
                    //throw exception
                    Log.d("Getting id error ", response.toString())
                }
            } catch (e:Exception) {
                //throw exception
                Log.d("Getting id error ", e.toString())
            }
            appInfo
        }


    suspend fun getToken(appId:String, appCerti:String, channelName:String):String =
        withContext(dispatcher) {
            var token = ""
            val request = AndroidNetworking.get("http://call-video-service.herokuapp.com/api/generateToken/appID=${appId}&appCertificate=${appCerti}&channelName=${channelName}")
                .build()
            try {
                val response = request.executeForJSONObject()
                if(response.isSuccess) {
                    val result = response.result as JSONObject
                    token = result.getString("token")
                    Log.d("Token 3: ", token)
                } else {
                    //throw exception
                    Log.d("Getting token error ", response.toString())
                }
            } catch (e:Exception) {
                Log.d("Getting token error ", e.toString())
            }
            token
        }

}