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
            val request = AndroidNetworking.get("https://lesson-service.herokuapp.com/api/lessons").build()
            try {
                val response = request.executeForJSONArray()
                if(response.isSuccess) {
                    Log.d("Get lessons start: ", "2")
                    val results = response.result as JSONArray
                    (0 until results.length()).forEach {
                        val item = results.getJSONObject(it)
                        val lesson = Lesson(item.getInt("id"), item.getString("channelName"), item.getInt("idTeacher"),
                            item.getInt("idStudent"), item.getString("timeStart"), item.getString("timeEnd")
                            ,item.getString("status"), item.getString("realTimeStart"), item.getString("realTimeEnd"))
                        lessonList.add(lesson)
                    }
                }
            } catch(exception:Exception) {
                Log.d("Get lessons list error: ", exception.toString())
            }
            lessonList
        }

    suspend fun getLessonById(id:Int):Lesson =
        withContext(dispatcher)  {
            val lesson:Lesson = Lesson()
            val request = AndroidNetworking.get("https://lesson-service.herokuapp.com/api/lessons/$id").build()
            try {
                val response = request.executeForJSONObject()
                if(response.isSuccess) {
                    Log.d("Get lessons start: ", "2")
                    val result = response.result as JSONObject
                    lesson.id = result.getInt("id")
                    lesson.channelName = result.getString("channelName")
                    lesson.idTeacher = result.getInt("idTeacher")
                    lesson.idStudent = result.getInt("idStudent")
                    lesson.timeStart = result.getString("timeStart")
                    lesson.timeEnd = result.getString("timeEnd")
                    lesson.status = result.getString("status")
                    lesson.realTimeStart = result.getString("realTimeStart")
                    lesson.realTimeEnd = result.getString("realTimeEnd")
                }
            } catch(exception:Exception) {
                Log.d("Get lessons list error: ", exception.toString())
            }
        lesson
    }


    suspend fun updateLesson(lesson: Lesson): Any =
        withContext(dispatcher) {
            Log.d("id: ", lesson.id.toString())
            var bodyObject:JSONObject = JSONObject()
            try {
                bodyObject.put("id", lesson.id)
                bodyObject.put("idStudent", lesson.idStudent)
                bodyObject.put("idTeacher", lesson.idTeacher)
                bodyObject.put("status", lesson.status)
                bodyObject.put("timeStart", lesson.timeStart)
                bodyObject.put("timeEnd", lesson.timeEnd)
                bodyObject.put("realTimeStart", lesson.realTimeStart)
                bodyObject.put("realTimeEnd", lesson.realTimeEnd)
                bodyObject.put("channelName", lesson.channelName)
                val request = AndroidNetworking.put("https://lesson-service.herokuapp.com/api/lessons/status/${lesson.id}").addJSONObjectBody(bodyObject).build()
                request.executeForJSONObject()
            } catch(e:Exception) {
                Log.d("Update lessons status list error: ", e.toString())
            }
        }
}