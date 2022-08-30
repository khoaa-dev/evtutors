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
import io.agora.rtc.video.VideoCanvas
import kotlinx.android.synthetic.main.activity_call.*
import kotlinx.coroutines.*

class Call : AppCompatActivity() {
    private var isCamera:Boolean=true
    var isMicro:Boolean=true
    private var lesson: Lesson?=null
    private var token:String=""
    private var appInfo = AgoraApp("", "")
    private var mRtcEngine:RtcEngine?=null
    private val callRepository = CallRepository(Dispatchers.IO)
    private val lessonRepository = LessonRepository(Dispatchers.IO)
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
            lessonRepository.updateLessonStatus(lesson!!.id, "1")
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
        override fun onUserOffline(uid: Int, reason: Int) {
//          We can handle event that show the form for rating the meeting here

            runOnUiThread{onRemoteUserLeft()}
        }

        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            runOnUiThread{ println("Join channel successfully: $uid")}
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

    private fun onRemoteUserLeft() {
        remote_video_view_container.removeAllViews()
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

    private fun handleFinish() {
        finish()
    }
}
