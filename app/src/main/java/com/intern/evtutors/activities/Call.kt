package com.intern.evtutors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.intern.evtutors.R
import com.intern.evtutors.data.CallRepository
import com.intern.evtutors.data.LessonRepository
import com.intern.evtutors.models.AgoraApp
import com.intern.evtutors.models.Lesson
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.internal.LastmileProbeConfig
import io.agora.rtc.ScreenCaptureParameters
import io.agora.rtc.mediaio.AgoraDefaultSource
import io.agora.rtc.video.VideoCanvas
import kotlinx.android.synthetic.main.activity_call.*
import kotlinx.coroutines.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Call : AppCompatActivity() {
    private var isCamera:Boolean=true
    var isMicro:Boolean=true
    var isshare:Boolean=true
    private var lesson: Lesson?=null
    private var token:String=""
    private var appInfo = AgoraApp("", "")
    private var mRtcEngine:RtcEngine?=null
    private val callRepository = CallRepository(Dispatchers.IO)
    private val lessonRepository = LessonRepository(Dispatchers.IO)
    private var numberAttendants:Int=1
    private var totalDuration:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)
        isCamera = intent.getBooleanExtra("camStatus", true)
        isMicro = intent.getBooleanExtra("micStatus", true)
        lesson = intent.getSerializableExtra("lesson") as Lesson

        startAgoraEngineAndJoin()

        camera.setOnClickListener(View.OnClickListener { view ->
            isCamera=!isCamera
            handleCameraOnOff()
        });
        mic.setOnClickListener(View.OnClickListener { view ->
            isMicro=!isMicro
            handleMicroOnOff()
        });

        call.setOnClickListener {
            handleFinish()
        }
        share.setOnClickListener(View.OnClickListener { View->
            evenShare()
            isshare=!isshare
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        mRtcEngine!!.leaveChannel()
        RtcEngine.destroy()
        mRtcEngine = null
    }

    private fun startAgoraEngineAndJoin() {
        //COROURTINE KOTLIN
        val scope = CoroutineScope(Dispatchers.Main + Job())
        //Need to handle exception
        scope.launch {
            //checking channel name
            createToken()
            initializeAgoraEngine()
            mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
            mRtcEngine!!.setClientRole(1)
            mRtcEngine!!.enableVideo()
            mRtcEngine!!.enableAudio()
            setupLocalVideo()
            joinChannel()
            checkingChannelStatus()
            handleMicroOnOff()
            handleCameraOnOff()
            progress_bar_layout.isVisible = false
        }
    }

    private suspend fun createToken() {
        Log.d("Start token: ", "1")
        appInfo = callRepository.getAppInfo()
        token = callRepository.getToken(appInfo.appId, appInfo.appCerti, lesson!!.channelName)
        Log.d("End token: ", "4")
    }

    private suspend fun checkingChannelStatus() {
        //considering:
        if(lesson!!.status == "0") {
            lessonRepository.updateLesson(lesson!!)
        }
    }


    private fun joinChannel() {
        mRtcEngine!!.joinChannel(token, lesson!!.channelName, null, 0)
    }

    private fun initializeAgoraEngine() {
        try {
            mRtcEngine = RtcEngine.create(baseContext, appInfo.appId, mRtcEngineHandler)
        } catch (e:Exception) {
            Log.d("Creating RTC Engine error: ", e.message.toString())
        }
    }

    //Handle every events in streaming
    private val mRtcEngineHandler:IRtcEngineEventHandler = object:IRtcEngineEventHandler() {
        override fun onUserJoined(uid: Int, elapsed: Int) {
            runOnUiThread{setupRemoteVideo(uid)}
        }

        override fun onLeaveChannel(stats: RtcStats?) {
//          We can handle event that show the form for rating the meeting here
            runOnUiThread{onRemoteUserLeft()}
        }
        override fun onUserOffline(uid: Int, reason: Int) {
            runOnUiThread{onRemoteUserLeft()}
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread{handleLessonStarted()}
        }

        override fun onRtcStats(stats: RtcStats) {
            runOnUiThread{handleLessonStatistic(stats)}
        }
    }

    private fun setupRemoteVideo(uid:Int) {
        if(remote_video_view_container.childCount >=1) {
            return
        }
        val surfaceView = RtcEngine.CreateRendererView(baseContext)
        remote_video_view_container.addView(surfaceView)
        mRtcEngine!!.setupRemoteVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, uid))
    }

    private fun setupLocalVideo() {
        var surfaceView = RtcEngine.CreateRendererView(baseContext)
        surfaceView.setZOrderMediaOverlay(true)
        local_video_view_container.addView(surfaceView)
        mRtcEngine!!.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_FIT, 0))
    }

    private fun handleLessonStarted() {
        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        coroutineScope.launch {
            val lessonUpdated = lessonRepository.getLessonById(lesson!!.id)
            Log.d("lesson id", lessonUpdated.id.toString()  )
            if(lessonUpdated.status != "1") {
                lessonUpdated.status = "1"
                lessonRepository.updateLesson(lessonUpdated)
            }
        }
    }

    private fun onRemoteUserLeft() {
        val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        coroutineScope.launch {
            val lessonUpdated = lessonRepository.getLessonById(lesson!!.id)
            val date = Date()
            if(numberAttendants<=1) {
                Log.d("lesson attendant", numberAttendants.toString())
                if(lessonUpdated.realTimeStart == "0000-00-00 00:00:00") {
                    lessonUpdated.realTimeStart = getRealTimeStart(totalDuration, date)
                }
                lessonUpdated.status = "2"
                lessonUpdated.realTimeEnd = formatDateTime(date)
                Log.d("lesson", lessonUpdated.toString())
            }
            lessonRepository.updateLesson(lessonUpdated)
        }
    }

    private fun getRealTimeStart(totalDuration:Int, date:Date):String {
        val result = Date(date.time - (totalDuration*1000))
        return formatDateTime(result)
    }

    private fun formatDateTime(date:Date):String {
        val dateFormat:DateFormat = SimpleDateFormat("yyyy-M-dd hh:mm:ss")
        return dateFormat.format(date)

    }

    private fun handleLessonStatistic(stats: IRtcEngineEventHandler.RtcStats) {
        numberAttendants = stats.users
        totalDuration = stats.totalDuration
    }


    private fun handleCameraOnOff() {
        if(isCamera) {
            camera.setImageResource(R.drawable.ic_camera)
            setupLocalVideo()
            mRtcEngine!!.enableLocalVideo(true)
        } else {
            camera.setImageResource(R.drawable.ic_stopcamera)
            local_video_view_container.removeAllViews()
            mRtcEngine!!.enableLocalVideo(false)
        }
    }

    private fun handleMicroOnOff() {
        if(isMicro) {
            mic.setImageResource(R.drawable.ic_mic)
            mRtcEngine!!.enableLocalAudio(true)
        } else {
            mic.setImageResource(R.drawable.ic_mute)
            mRtcEngine!!.enableLocalAudio(false)
        }
    }
    private fun evenShare(){
        if(isshare){
            share.setImageResource(R.drawable.ic_share_start)
            shareScreen()
        }else{
            share.setImageResource(R.drawable.ic_share)
            stopshareScreen()
        }
    }
    private fun shareScreen(){
        mRtcEngine!!.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
        mRtcEngine!!.setClientRole(IRtcEngineEventHandler.ClientRole.CLIENT_ROLE_BROADCASTER)
        val screenCaptureParameters = ScreenCaptureParameters()
        screenCaptureParameters.captureAudio = true
        screenCaptureParameters.captureVideo = true
        val videoCaptureParameters: ScreenCaptureParameters.VideoCaptureParameters =
            ScreenCaptureParameters.VideoCaptureParameters()
        screenCaptureParameters.videoCaptureParameters = videoCaptureParameters
        mRtcEngine!!.startScreenCapture(screenCaptureParameters)
    }
    private fun stopshareScreen(){
        mRtcEngine!!.stopScreenCapture()
        mRtcEngine!!.setVideoSource(AgoraDefaultSource())
    }

    private fun handleFinish() {
        finish()
    }
}
