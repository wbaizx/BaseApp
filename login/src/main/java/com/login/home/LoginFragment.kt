package com.login.home

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.callback.NavCallback
import com.base.common.base.mvvm.BaseMVVMFragment
import com.base.common.util.SharedPreferencesUtil
import com.base.common.util.imageload.imgUrl
import com.base.common.util.imageload.loadImg
import com.base.common.util.launchARouter
import com.base.common.util.log
import com.base.common.util.normalNavigation
import com.login.R
import com.login.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "LoginFragment"

class LoginFragment : BaseMVVMFragment<LoginViewModel, FragmentLoginBinding>() {
    override val vm by viewModel<LoginViewModel>()

    override fun bindModelId(binding: FragmentLoginBinding) {
        binding.vm = vm
    }

    override fun initObserve() {
        vm.successBean.observe(this) {
            log(TAG, "loginSuccessBean -- ${it.data.id}")
            loginSuccess()
        }

        vm.successResponse.observe(this) { (r1, r2) ->
            log(TAG, "loginSuccessResponseBody -- ${r1.string()} -- ${r2.string()}")
            loginSuccess()
        }
    }

    override fun getContentView() = R.layout.fragment_login

    override fun createView() {
    }

    override fun onFirstVisible() {
        log(TAG, "onFirstVisible")

        loginImg.loadImg(imgUrl)

        loginBtn.setOnClickListener {
//            vm.loginBean()
            vm.loginResponseBody()
        }
    }

    override fun onVisible() {
        log(TAG, "onVisible")
    }

    override fun onHide() {
        log(TAG, "onHide")
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