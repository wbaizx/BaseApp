package com.login.home

import android.view.View
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.base.common.base.fragment.BaseBindModelFragment
import com.base.common.util.*
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadImg
import com.login.R
import com.login.databinding.FragmentLoginBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "LoginFragment"

class LoginFragment : BaseBindModelFragment<LoginViewModel, FragmentLoginBinding>() {
    override val vm by viewModel<LoginViewModel>()

    override fun viewBind(binding: FragmentLoginBinding) {
        binding.vm = vm
    }

    override fun initObserve() {
        vm.successBean.lifecycleCollect(this) {
            debugLog(TAG, "loginSuccessBean -- ${it.data.id}")
            loginSuccess()
        }

        vm.successResponse.lifecycleCollect(this) { (r1, r2) ->
            debugLog(TAG, "loginSuccessResponseBody -- ${r1.string()} -- ${r2.string()}")
            loginSuccess()
        }
    }

    override fun getContentView() = R.layout.fragment_login

    override fun createView(view: View) {
    }

    override fun onFirstVisible() {
        debugLog(TAG, "onFirstVisible")

        binding.loginImg.loadImg(imgUrl)

        binding.loginBtn.setOnClickListener {
//            vm.loginBean()
            vm.loginResponseBody()
        }
    }

    override fun onVisible() {
        debugLog(TAG, "onVisible")
    }

    override fun onHide() {
        debugLog(TAG, "onHide")
    }

    private fun loginSuccess() {
        SharedPreferencesUtil.putData(SharedPreferencesUtil.LOGIN, true)

        launchARouter("/main/main_home").normalNavigation(requireContext(), navCallback = object : NavCallback() {
            override fun onArrival(postcard: Postcard?) {
                activity?.finish()
            }
        })
    }
}