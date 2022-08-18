package com.intern.evtutors.models

class Courses (_className:String,_timeBegin:String, _timeEnd:String, _tutor:String, _status:Int){

//This is a demo so all the attributes here must be changed
    var className:String

    //must be replaced by "time"
    var timeBegin:String
    var timeEnd:String
    //

    var tutor:String
    //must be replace by a status logic
    var status:Int

    var channelName:String
    init {
        className = _className
        timeBegin = _timeBegin
        timeEnd = _timeEnd
        tutor = _tutor
        status = _status

        //considering how the className look like...
        channelName = "${_className}_${_tutor}_${_timeBegin}"
    }
}