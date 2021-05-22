package com.power.tesseract.ui.main

import android.content.ComponentName
import android.content.Context.BIND_AUTO_CREATE
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.CountDownTimer
import android.os.IBinder
import android.os.RemoteException
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.power.tesseract.IMyAidlInterface
import com.power.tesseract.R
import com.power.tesseract.databinding.AidlBindingFragmentBinding
import com.power.tesseract.viewmodels.AidlViewModel

class AidlBindingFragment : Fragment() {
    companion object {
        fun newInstance() = AidlBindingFragment()
        const val REQUEST_AIDL_PACKAGE = "com.power.phoneorientationdata.server"
    }

    private var mCountDownTimer: CountDownTimer? = null
    private lateinit var viewModel: AidlViewModel
    private var mICommon: IMyAidlInterface? = null

    private val serviceConn = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            mICommon = IMyAidlInterface.Stub.asInterface(binder)
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(AidlViewModel::class.java)
        val binding = AidlBindingFragmentBinding.inflate(inflater, container, false)

        initViewClicks(binding)
        return binding.root
    }

    private fun initViewClicks(binding: AidlBindingFragmentBinding) {
        binding.btnBind.apply {
            setOnClickListener {
                bindWithService(binding)
            }
        }
        binding.btnRequestData.apply {
            setOnClickListener {
                try {
                    if (mCountDownTimer == null) {
                        mCountDownTimer = object : CountDownTimer(1000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                            }

                            override fun onFinish() {
                                mICommon?.let {
                                    binding.groupOfViews.visibility = View.VISIBLE
                                    binding.tvErrorDisplay.visibility = View.GONE
                                    binding.tvTotalResult.text = " Raw :- ${it.requestValues()}"
                                    binding.tvXAxis.text = "X = ${it.requestXAxis()}"
                                    binding.tvYAxis.text = "Y = ${it.requestYAxis()}"
                                    binding.tvZAxis.text = "Z = ${it.requestZAxis()}"
                                    binding.tvAccuracy.text = "Accuracy =${it.requestAccuracy()}"
                                }
                                if (mICommon == null) {
                                    handleErrorUserCase(
                                        binding,
                                        requireActivity().getString(R.string.aidl_conn_error)
                                    )
                                }
                                mCountDownTimer!!.start()
                            }
                        }
                    }
                    mCountDownTimer?.apply {
                        cancel()
                        start()
                    }
                } catch (e: RemoteException) {
                    // Handle visibilty of Groups.
                    handleErrorUserCase(
                        binding,
                        requireActivity().getString(R.string.aidl_conn_error)
                    )
                }
            }
        }
    }

    private fun bindWithService(binding: AidlBindingFragmentBinding) {
        val intent = Intent(REQUEST_AIDL_PACKAGE)
        val respIntent = convertImplicitToExplicitIntent(intent)?.let {
            requireActivity().bindService(
                it,
                serviceConn,
                BIND_AUTO_CREATE
            )
            binding.btnRequestData.isEnabled = true
            it
        }

        if (respIntent == null) {
            binding.btnRequestData.isEnabled = false
            handleErrorUserCase(
                binding,
                requireActivity().getString(R.string.not_installed_app_error)
            )
        }
    }

    private fun handleErrorUserCase(binding: AidlBindingFragmentBinding, str: String) {
        binding.groupOfViews.visibility = View.GONE
        binding.tvErrorDisplay.visibility = View.VISIBLE
        binding.tvErrorDisplay.text = str

    }

    private fun convertImplicitToExplicitIntent(implicitIntent: Intent): Intent? {
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

    override fun onResume() {
        super.onResume()
        mCountDownTimer?.apply {
            cancel()
            start()
        }
    }

    override fun onStart() {
        super.onStart()
        mCountDownTimer?.cancel()
    }

}