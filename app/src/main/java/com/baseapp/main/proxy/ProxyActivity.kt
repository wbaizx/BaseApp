package com.baseapp.main.proxy

import androidx.lifecycle.lifecycleScope
import com.base.common.base.activity.BaseBindActivity
import com.base.common.helper.safeLaunch
import com.base.common.helper.setOnSingleClickListener
import com.baseapp.R
import com.baseapp.databinding.ActivityProxyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.Socket
import java.util.regex.Pattern

/**
 * socks5代理相关
 */
class ProxyActivity : BaseBindActivity<ActivityProxyBinding>() {

    override fun getContentView() = R.layout.activity_proxy

    override fun viewBind(binding: ActivityProxyBinding) {
    }

    override fun initView() {
        binding.checkConnect.setOnSingleClickListener {
            lifecycleScope.safeLaunch(catch = {
                binding.connect.text = "error $it"

            }) {
                withContext(Dispatchers.IO) {
                    checkConnect(
                        binding.userName.text.toString(),
                        binding.userPassword.text.toString(),
                        binding.ip.text.toString(),
                        binding.port.text.toString().toInt()
                    )
                }
                binding.connect.text = "success"
            }
        }

        binding.checkSpeed.setOnSingleClickListener {
            lifecycleScope.safeLaunch {
                binding.speed.text = withContext(Dispatchers.IO) { checkSpeed(binding.ip.text.toString()) }
            }
        }

        //netty socks5代理核心
//        val address = InetSocketAddress(proxyIp, proxyPort)
//        pipeline.addFirst(Socks5ProxyHandler(address, userName, userPassword))


        //okhttp 代理核心（socks5没跑通）
//        val clientBuilder = OkHttpClient.Builder()
//        clientBuilder.proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress(proxyIp, proxyPort)))
//            .proxyAuthenticator(Authenticator { _, response ->
//                val basic = Credentials.basic("userName", "userPassword")
//                return@Authenticator response.request.newBuilder()
//                    .header("Proxy-Authorization", basic)
//                    .build()
//            })


        //参考 https://zhuanlan.zhihu.com/p/33726379?utm_id=0
        //https://blog.csdn.net/cwjcsu/article/details/79279783
        //https://stackoverflow.com/questions/37866902/okhttp-proxy-settings

        //http代理可以参考
        //设置 clientBuilder.proxy(Proxy(Proxy.Type.SOCKS, InetSocketAddress(proxyIp, proxyPort)))
        //然后在 okhttp addInterceptor 拦截器中配置线程隔离的 ProxyAuthenticator
        //或者自定义 SocketFactory
        //https://www.cnblogs.com/cwjcsu/p/add-socks-proxy-support-for-okhttp.html
    }

    /**
     * 测连通性
     */
    private fun checkConnect(userName: String, userPassword: String, proxyIp: String, proxyPort: Int): Boolean {
        ProxyAuthenticator.setPasswordAuthentication(userName, userPassword)

        Socket(Proxy(Proxy.Type.SOCKS, InetSocketAddress(proxyIp, proxyPort))).use {
            it.connect(InetSocketAddress("1.1.1.1", 1))
        }

        return true
    }

    /**
     * 测速
     */
    private fun checkSpeed(proxyIp: String): String {
        val p = Runtime.getRuntime().exec("ping -c 1 $proxyIp")
        val status = p.waitFor()

        val sb = StringBuilder()
        BufferedReader(InputStreamReader(p.inputStream)).use {
            while (it.readLine().apply { if (this != null) sb.append(this) } != null) {
            }
        }

        val matcher = Pattern.compile("time=(((?!time=).)+?) ms").matcher(sb)
        val speed = if (matcher.find()) matcher.group(1) else null

        return "status $status - ${speed}ms"
    }
}