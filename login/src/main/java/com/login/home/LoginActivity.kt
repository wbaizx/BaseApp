package com.login.home

import androidx.lifecycle.Lifecycle
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.base.common.base.BaseActivity
import com.base.common.util.http.ObjectBean
import com.base.common.util.http.ParcelableBean
import com.base.common.util.http.SerializableBean
import com.base.common.util.log
import com.login.R

private const val TAG = "LoginActivity"

@Route(path = "/login/login_home", name = "组件化登录模块首页")
class LoginActivity : BaseActivity() {
    @JvmField
    @Autowired(name = "serializable_bean")
    var sb: SerializableBean? = null

    @JvmField
    @Autowired(name = "parcelable_bean")
    var pb: ParcelableBean? = null

    @JvmField
    @Autowired(name = "object_bean")
    var ob: ObjectBean? = null

    override fun getContentView() = R.layout.activity_login

    override fun initView() {
        log(TAG, "auto wired $sb")
        log(TAG, "auto wired $pb")
        log(TAG, "auto wired $ob")

        val loginFragment = LoginFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.frameLayout, loginFragment)
            .setMaxLifecycle(loginFragment, Lifecycle.State.RESUMED)
            .commit()
    }
}
