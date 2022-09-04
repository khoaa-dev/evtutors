package com.intern.evtutors.models

import java.io.Serializable

data class Lesson (var id:Int, var channelName:String, var idTeacher:Int, var idStudent:Int, var timeStart:String, var timeEnd:String,
              var status:String, var realTimeStart:String, var realTimeEnd:String):Serializable {
    constructor() : this(0, "", 0, 0, "","", "", "", "")
}
//{
//
////This is a demo so all the attributes here must be changed
//    var id:Int
//    //must be replaced by "time"
//    var idStudent:Int
//    var idTeacher:Int
//    //
//    var status:String
//    var timeStart:String
//    var timeEnd:String
//    var realTimeStart:String
//    var realTimeEnd:String
//    var channelName:String
//    init {
//        id = _id
//        timeStart = _timeBegin
//        timeEnd = _timeEnd
//        realTimeStart = _realTimeStart
//        realTimeEnd = _real/TimeEnd
//        idTeacher = _idTeacher
//        idStudent = _idStudent
//        status = _status
//        //considering how the className look like...
//        channelName = _channelName
//    }
//
//    constructor() {
//
//    }
//}