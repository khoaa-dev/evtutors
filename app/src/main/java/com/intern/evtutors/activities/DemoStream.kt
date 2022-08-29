package com.intern.evtutors.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.intern.evtutors.MainActivity
import com.intern.evtutors.R
import kotlinx.android.synthetic.main.activity_demo_stream.*
import org.json.JSONArray

class DemoStream : AppCompatActivity() {
    //state of camera and micro
    private var cameraStatus:Boolean = true
    private var microStatus:Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_stream)
        getAppId()
        //Allowing permission
        if(!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 22)
        }
        startCamera()
        btn_camera.setOnClickListener {
            cameraStatus=!cameraStatus
            handleOnclickCamera()
        }

        btn_micro.setOnClickListener {
            microStatus=!microStatus
            handleOnclickMicro()
        }

        btn_cancel.setOnClickListener {
            var intent: Intent = Intent(baseContext, MainActivity::class.java)
            Toast.makeText(baseContext,"fafds", Toast.LENGTH_SHORT)
            startActivity(intent)
        }

        btn_start.setOnClickListener {
            val channelName = intent.getStringExtra("channelName").toString()
            val intent = Intent(this, Call::class.java)
            intent.putExtra("micStatus", microStatus)
            intent.putExtra("camStatus", cameraStatus)
            intent.putExtra("channelName", channelName)
            startActivity(intent)
        }
    }

    private fun handleOnclickCamera() {
        previewView.isVisible = cameraStatus
        txt_camera.isVisible = !cameraStatus
        if(cameraStatus) {
            btn_camera.setImageResource(R.drawable.ic_camera2)
        } else {
            btn_camera.setImageResource(R.drawable.ic_uncapture)
        }
    }

    private fun handleOnclickMicro() {
        if(microStatus) {
            btn_micro.setImageResource(R.drawable.ic_micro)
        } else {
            btn_micro.setImageResource(R.drawable.ic_unvoice)
        }
    }


    //    handle start camera
    private fun startCamera() {
        // Sử dụng để ràng buộc vòng đời của máy ảnh với vòng đời của View. Điều này giúp loại bỏ nhiệm vụ mở và đóng máy ảnh vì CameraX nhận biết được vòng đời.
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {

            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            // Lựa chọn mặc định dùng camera sau.
            val cameraSelector:CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            // Bên trong khối try, hãy đảm bảo không có gì liên kết với cameraProvider, sau đó liên kết cameraSelector và đối tượng Preview với cameraProvider.
            try {
                // Huỷ liên kết với vòng đời của View trước khi liên kết trở lại
                cameraProvider.unbindAll()

                // Liên kết Preview use case đến Camera.
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview)

            } catch(exc: Exception) {
                Log.e("camera preview", "Liên kết thất bại", exc)
            }

        }, ContextCompat.getMainExecutor(this)) //Chạy trên luồng chính.
    }


    //    checking all camera permissions
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED

    }

    private fun getAppId() {
        val sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        AndroidNetworking.get("http://call-video-service.herokuapp.com/api/agoraApp")
            .build()
            .getAsJSONArray(object : JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    editor.putString("APP_ID", response!!.getJSONObject(0).getString("appID").toString())
                    editor.putString("APP_CERTIFICATE", response!!.getJSONObject(0).getString("appCertificate").toString())
                    editor.commit()
                    var id=response!!.getJSONObject(0).getString("appID").toString()

                }
                override fun onError(anError: ANError?) {
                    Log.d("Get app id error: ", anError.toString())
                }

            })


    }


    //COMPANION OBJECT: consist of methods that we want to use without creating 'class'
     companion object {
            private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
     }
}