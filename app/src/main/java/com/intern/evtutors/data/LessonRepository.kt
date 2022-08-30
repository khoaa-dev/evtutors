package com.intern.evtutors.data

import android.util.Log
import com.androidnetworking.AndroidNetworking
import com.intern.evtutors.models.Lesson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

data class LessonRepository(private val dispatcher: CoroutineDispatcher) {
     suspend fun getAllLessons():MutableList<Lesson> =
        withContext(dispatcher)  {
            var lessonList:MutableList<Lesson> = ArrayList()
            val request = AndroidNetworking.get("https://call-video-service.herokuapp.com/api/lessons").build()
            try {
                val response = request.executeForJSONArray()
                if(response.isSuccess) {
                    Log.d("Get lessons start: ", "2")
                    val results = response.result as JSONArray
                    (0 until results.length()).forEach {
                        val item = results.getJSONObject(it)
                        val lesson = Lesson(item.getInt("id"), item.getString("channelName"), item.getInt("idTeacher"),
                            item.getInt("idStudent"), item.getString("timeStart"), item.getString("timeEnd"), item.getString("status"))
                        lessonList.add(lesson)
                    }
                }
            } catch(exception:Exception) {
                Log.d("Get lessons list error: ", exception.toString())
            }
            lessonList
        }

    suspend fun updateLessonStatus(id:Int, status:String): Any =
        withContext(dispatcher) {
            Log.d("id: ", id.toString())
            var bodyObject:JSONObject = JSONObject()
            try {
                bodyObject.put("id", 1)
                bodyObject.put("idStudent", 123)
                bodyObject.put("idTeacher", 321)
                bodyObject.put("status", status)
                bodyObject.put("timeStart", "2021-07-26 17:00:00")
                bodyObject.put("timeEnd", "2021-07-26 19:00:00")
                bodyObject.put("channelName", "123-321-2607")
                val request = AndroidNetworking.put("https://call-video-service.herokuapp.com/api/lessons/status/$id").addJSONObjectBody(bodyObject).build()
                request.executeForJSONObject()
            } catch(e:Exception) {
                Log.d("Update lessons status list error: ", e.toString())
            }
        }

    suspend fun updateLessonTimeStart() {

    }

    suspend fun updateLessonTimeEnd() {

    }

}