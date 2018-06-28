package cn.jesse.gaea.plugin.main.ui.fragment

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jesse.gaea.lib.base.router.ActivityRouter
import cn.jesse.gaea.lib.base.ui.BaseFragment
import cn.jesse.gaea.lib.base.util.AppUtil
import cn.jesse.gaea.lib.base.util.ContextUtil
import cn.jesse.gaea.lib.common.constant.RemoteRouterDef
import cn.jesse.gaea.lib.common.util.AtlasUpdateUtil
import cn.jesse.gaea.lib.common.vm.UpdateViewModel
import cn.jesse.gaea.plugin.main.R
import cn.jesse.gaea.plugin.main.constant.PluginDef
import cn.jesse.nativelogger.NLogger
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment_main.*

/**
 * 首页Fragment
 *
 * @author Jesse
 */
class MainFragment : BaseFragment() {
    private var loginStatus = false
    private lateinit var updateViewModel: UpdateViewModel

    override fun getLogTag(): String {
        return "${PluginDef.TAG}.MainFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.main_fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateViewModel = ViewModelProviders.of(this).get(UpdateViewModel::class.java)

        btnLogin.setOnClickListener {
            ActivityRouter.startActivity(this, RemoteRouterDef.PluginUser.ACTIVITY_LOGIN, RemoteRouterDef.PluginUser.CODE_LOGIN_STATUS)
        }

        showLoginStatus()

        if (!"1.0.0".equals(AppUtil.getVersionName(ContextUtil.getApplicationContext()))) {
            btnLoadPatch.visibility = View.GONE
            return
        }

        btnLoadPatch.setOnClickListener {
            updateViewModel.checkUpdate(UpdateViewModel.Mode.PATCH)
        }
    }

    /**
     * 显示用户登录状态
     */
    private fun showLoginStatus() {
        if (loginStatus) {
            tvLoginStatus.text = "(已登录)"
            tvLoginStatus.setTextColor(Color.GREEN)
        } else {
            tvLoginStatus.text = "(未登录)"
            tvLoginStatus.setTextColor(Color.RED)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RemoteRouterDef.PluginUser.CODE_LOGIN_STATUS && resultCode == Activity.RESULT_OK) {
            loginStatus = data?.extras?.getBoolean(RemoteRouterDef.PluginUser.PARAMS_LOGIN_STATUS)!!
            NLogger.d(mTag, "login status $loginStatus")
            showLoginStatus()
        }
    }

    companion object {
        fun newInstance(): BaseFragment {
            return MainFragment()
        }
    }
}