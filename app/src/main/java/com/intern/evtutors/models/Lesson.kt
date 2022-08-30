package com.intern.evtutors.models

import java.io.Serializable

class Lesson (_id:Int, _channelName:String, _idTeacher:Int, _idStudent:Int, _timeBegin:String, _timeEnd:String,  _status:String):Serializable{

//This is a demo so all the attributes here must be changed
    var id:Int

    //must be replaced by "time"
    var idStudent:Int
    var idTeacher:Int
    //

    var status:String

    var timeStart:String
    var timeEnd:String

    var channelName:String
    init {
        id = _id
        timeStart = _timeBegin
        timeEnd = _timeEnd
        idTeacher = _idTeacher
        idStudent = _idStudent
        status = _status
        //considering how the className look like...
        channelName = _channelName
    }

}