package com.power.tesseract.ui.main

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
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

        // List<ApplicationInfo> apps = getPackageManager().getInstalledPackages(0);
        //  val apps: List<ApplicationInfo> = getPackageManager().getInstalledApplications(0)
        // for (app in apps) {
        // if (app.flags and (ApplicationInfo.FLAG_UPDATED_SYSTEM_APP or ApplicationInfo.FLAG_SYSTEM) > 0) {
        //      // It is a system app
        //  } else {
        //  // It is installed by the user
        // }
        //  }
        // Flags: See below
        // Flags: See below
        // OR
//        val flags = PackageManager.GET_META_DATA or
//                PackageManager.GET_SHARED_LIBRARY_FILES or
//                PackageManager.GET_UNINSTALLED_PACKAGES
//
//        val pm: PackageManager = getPackageManager()
//        val applications = pm.getInstalledApplications(flags)
//        for (appInfo in applications) {
//            if (appInfo.flags and ApplicationInfo.FLAG_SYSTEM == 1) {
//                // System application
//            } else {
//                // Installed by user
//            }
//        }


        val packages =
            requireActivity().packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        for (packageInfo in packages) {
            if (ApplicationInfo.FLAG_SYSTEM == 1) {
                // System application
            } else {
                // Installed by user
            }
            val packageName = packageInfo.packageName
            Log.d("Package name", "Package name:$packageName")
            Log.d("logo:", "logo:" + packageInfo.logo)
            val icon: Drawable = requireContext().packageManager.getApplicationIcon(packageName)

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