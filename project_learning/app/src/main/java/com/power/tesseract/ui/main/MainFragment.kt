package com.power.tesseract.ui.main

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.power.tesseract.IMyAidlInterface
import com.power.tesseract.R


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    var mICommon: IMyAidlInterface? = null

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mICommon = IMyAidlInterface.Stub.asInterface(binder)
            Log.d("isBinded", mICommon.toString())
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }

    }

    fun convertImplicitToExplicitIntent(implicitIntent: Intent): Intent? {
        val listPackages =
            requireActivity().packageManager.queryIntentServices(implicitIntent, 0)

        if (listPackages.isEmpty()) {
            return null
        }
        val serviceInfo = listPackages[0]
        val component =
            ComponentName(serviceInfo.serviceInfo.packageName, serviceInfo.serviceInfo.name)
        val explicitIntent = Intent(implicitIntent)
        explicitIntent.component = component
        return explicitIntent

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val view = inflater.inflate(R.layout.main_fragment, container, false)

        view.findViewById<Button>(R.id.btn_bind).apply {
            setOnClickListener {
                val intent = Intent("com.power.phoneorientationdata.server")
                requireActivity().bindService(
                    convertImplicitToExplicitIntent(intent),
                    serviceConn,
                    BIND_AUTO_CREATE
                )
            }
        }
        view.findViewById<Button>(R.id.btn_calculate).apply {
            setOnClickListener {
                try {
                    mICommon?.let {
                        Log.d("CalculateMethod", "" + it.requestValues())
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
        return view
    }

    /**
     * Launch Activity
     */
    private fun launchApp(packageName: String?) {
        val mIntent: Intent? = requireActivity().packageManager.getLaunchIntentForPackage(
            packageName!!
        )
        try {
            startActivity(mIntent)
        } catch (err: ActivityNotFoundException) {
//                val t: Toast = Toast.makeText(ApplicationProvider.getApplicationContext(),
//                        R.string.app_not_found, Toast.LENGTH_SHORT)
//                t.show()
        }
    }

}