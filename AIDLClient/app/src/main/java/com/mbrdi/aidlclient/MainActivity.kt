package com.mbrdi.aidlclient

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.mbrdi.aidlserver.IAIDLColorInterface

class MainActivity : AppCompatActivity() {

    private var iAIDLColorInterface: IAIDLColorInterface? = null
    private val TAG: String = "AIDL Client"

    private val mConnection = object : ServiceConnection{
        override fun onServiceConnected(
            name: ComponentName?,
            service: IBinder?)
        {
            iAIDLColorInterface = IAIDLColorInterface.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            iAIDLColorInterface = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val intent = Intent("AIDLColorService")
        intent.setPackage("com.mbrdi.aidlserver")

        bindService(intent, mConnection, BIND_AUTO_CREATE)

        Log.d(TAG, "binder Service is connected")

        var mButton: Button = findViewById(R.id.button2)
        mButton.setOnClickListener { view ->
            try {
                val color = iAIDLColorInterface?.color?:return@setOnClickListener
                Log.d(TAG, "Color resposne : $~")
                view.setBackgroundColor(color)
            } catch (e: RemoteException) {
                Log.e(TAG, "RemoteException: ${e.message}")
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        unbindService(mConnection)
//    }
}