package com.example.myapplicationview.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplicationview.R
import com.example.myapplicationview.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private data class TabSpec(val menuId: Int, val navGraphResId: Int)

    private val tabs = listOf(
        TabSpec(R.id.chatWx_graph, R.navigation.nav_chat),
        TabSpec(R.id.contact_graph, R.navigation.nav_contact),
        TabSpec(R.id.find_graph, R.navigation.nav_find),
        TabSpec(R.id.me_graph, R.navigation.nav_me)
    )
    private val tabMenuIds by lazy { tabs.map { it.menuId } }
    private val fragmentTags by lazy { tabs.indices.map { i -> "bottomNav#${i}" } }
    private val idToIndex by lazy { tabs.mapIndexed { index, tab -> tab.menuId to index }.toMap() }
    private var currentTag: String = ROOT_TAG // 初始显示 root host (login)

    private var currentNavController: NavController? = null
    // 在程序性设置 BottomNavigation 选中项时抑制回调
    private var suppressBottomSelection: Boolean = false
    // 标记当前是否在 Tab 模式
    private var isInTabMode: Boolean = false
    // 返回键处理回调
    private lateinit var backPressedCallback: OnBackPressedCallback

    private val rootDestinationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "rootDestinationListener: destination=${destination.id}")
            // 如果 root NavController 导航到了任意一个子 navigation 的开始页面或其子目的地，切换到 tab hosts
            // 支持直接跳转到 chat_graph / contact_graph / find_graph / me_graph 中的任意一个
            val index = findTabIndex(destination)
            if (index != null) {
                Log.d("MainActivity", "rootDestinationListener: switching to tabs index=$index")
                switchToTabs(selectedIndex = index)
                return@OnDestinationChangedListener
            }

            // 在根导航（登录或其他页面）时隐藏底部栏
            setBottomNavVisible(false)
        }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "destinationChangedListener: destination=${destination.id}")
            // 在 tab NavController 中，当目的地是 loginFragment 时隐藏 bottomBar（通常不会发生）
            setBottomNavVisible(destination.id != R.id.loginFragment)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(viewBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            currentTag = savedInstanceState.getString(SELECTED_TAG, currentTag) ?: ROOT_TAG
        }

        // 初始化返回键处理
        initBackPressedCallback()

        // 初始创建 root NavHost 显示 loginFragment
        setupRootNavHost()
        setupBottomNav()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SELECTED_TAG, currentTag)
        super.onSaveInstanceState(outState)
    }

    /**
     * 初始化返回键处理回调
     * 用于在 Tab 模式下处理返回键，确保返回栈为空时退到桌面而不是杀掉 App
     */
    private fun initBackPressedCallback() {
        backPressedCallback = object : OnBackPressedCallback(false) {
            override fun handleOnBackPressed() {
                Log.d("MainActivity", "handleOnBackPressed: isInTabMode=$isInTabMode")

                if (isInTabMode && currentNavController != null) {
                    val navController = currentNavController!!
                    val hasBackStack = navController.previousBackStackEntry != null

                    if (hasBackStack) {
                        // 返回栈不为空，正常返回
                        Log.d("MainActivity", "handleOnBackPressed: navigating back in NavController")
                        navController.navigateUp()
                    } else {
                        // 返回栈为空，退到桌面
                        Log.d("MainActivity", "handleOnBackPressed: back stack is empty, moving to back of task stack")
                        moveTaskToBack(true)
                    }
                } else {
                    // 不在 Tab 模式或 NavController 不可用，退到桌面
                    Log.d("MainActivity", "handleOnBackPressed: not in tab mode, moving to back of task stack")
                    moveTaskToBack(true)
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun setupRootNavHost() {
        val fm = supportFragmentManager
        var rootHost = fm.findFragmentByTag(ROOT_TAG) as NavHostFragment?
        if (rootHost == null) {
            rootHost = NavHostFragment.create(R.navigation.nav_graph)
            fm.beginTransaction()
                .add(R.id.nav_host_fragment, rootHost, ROOT_TAG)
                .commitNow()
        }

        // 显示 root 并设置为 primary
        fm.beginTransaction()
            .show(rootHost)
            .setPrimaryNavigationFragment(rootHost)
            .commitNow()

        // 监听 root nav controller 的目的地变化
        val navController = rootHost.navController
        navController.addOnDestinationChangedListener(rootDestinationListener)

        // 隐藏 bottomNav 在登录页面
        setBottomNavVisible(false)
        currentNavController = navController
    }

    private fun setupNavHosts() {
        val fm = supportFragmentManager

        tabs.forEachIndexed { index, tab ->
            val tag = fragmentTags[index]
            var host = fm.findFragmentByTag(tag) as NavHostFragment?

            if (host == null) {
                // 1. 创建空的 NavHostFragment
                host = NavHostFragment.create(0)

                fm.beginTransaction()
                    .add(R.id.nav_host_fragment, host, tag)
                    .hide(host)
                    .commitNow()

                // 3. 设置图。NavController 会自动导航到该 XML 里的 startDestination
                // 只要 nav_chat.xml 里的 startDestination 不是 login，就不会显示登录页
                host.navController.setGraph(tab.navGraphResId)

                Log.d("MainActivity", "setupNavHosts: Tag $tag loaded Graph ${resources.getResourceEntryName(tab.navGraphResId)}")
            }
        }
    }

    private fun switchToTabs(selectedIndex: Int) {
        Log.d("MainActivity", "switchToTabs: selectedIndex=$selectedIndex")
        // 创建并显示 tab hosts（如果尚未创建），然后移除 root host
        setupNavHosts()

        val fm = supportFragmentManager
        // 隐藏并移除 root host
        val root = fm.findFragmentByTag(ROOT_TAG)
        if (root != null) {
            // remove the root fragment to avoid conflicting NavControllers
            // 先从 root 的 NavController 上移除对 rootDestinationListener 的监听，避免它在移除后继续影响 UI
            try {
                val rootHost = root as? NavHostFragment
                rootHost?.navController?.removeOnDestinationChangedListener(rootDestinationListener)
            } catch (e: Exception) {
                // ignore
            }
            // 显示选中的 tab
            val newTag = fragmentTags[selectedIndex]
            val newFrag = fm.findFragmentByTag(newTag) as NavHostFragment
            // 先 show 新的 fragment 并设置为 primary，以便它先渲染在容器中
            fm.beginTransaction()
                .show(newFrag)
                .setPrimaryNavigationFragment(newFrag)
                .commitNow()

            // 先更新 currentTag，避免监听器读取到已删除的 root tag
            currentTag = newTag

            // 在确保新的 fragment 已显示后再移除 root（这样可以避免 root 的视图遮挡 bottomNav）
            if (root != null) {
                try {
                    fm.beginTransaction()
                        .remove(root)
                        .commitNow()
                    Log.d("MainActivity", "switchToTabs: removed root fragment after showing newFrag")
                } catch (e: Exception) {
                    Log.d("MainActivity", "switchToTabs: failed to remove root fragment: ${e.message}")
                }
            }
        }

        // 强制显示 BottomNavigation 并设置选中项为对应的 navGraph id，保证在任何子图跳转后都可见
        setBottomNavVisible(true)
        val menuId = tabs.getOrNull(selectedIndex)?.menuId ?: tabs.first().menuId
        updateBottomNavSelection(menuId)
        // 确保 bottomNav 在最上层，避免被 fragment 覆盖（延迟到 UI 线程队列在事务完成后执行）
        viewBinding.bottomNav.post {
            try {
                viewBinding.bottomNav.bringToFront()
                viewBinding.bottomNav.elevation = resources.getDimension(R.dimen.bottom_nav_elevation)
                viewBinding.bottomNav.requestLayout()
                viewBinding.bottomNav.invalidate()
                Log.d("MainActivity", "switchToTabs: bottomNav brought to front and refreshed")
            } catch (e: Exception) {
                Log.d("MainActivity", "switchToTabs: failed to bring bottomNav to front: ${e.message}")
            }
        }

        // 清理旧的监听器，防止竞争（再次确保当前 NavController 指向新 fragment）
        try {
            currentNavController?.removeOnDestinationChangedListener(destinationChangedListener)
        } catch (e: Exception) {
            // ignore
        }

        setNavListenerForTag(currentTag)

        // 标记进入 Tab 模式并启用返回键处理
        isInTabMode = true
        backPressedCallback.isEnabled = true
        Log.d("MainActivity", "switchToTabs: entered tab mode, back pressed callback enabled")
    }

    private fun setupBottomNav() {
        val bottomNav = viewBinding.bottomNav
        bottomNav.setOnItemSelectedListener { item ->
            if (suppressBottomSelection) return@setOnItemSelectedListener true
            val newIndex = idToIndex[item.itemId] ?: return@setOnItemSelectedListener false
            val newTag = fragmentTags[newIndex]
            if (newTag == currentTag) return@setOnItemSelectedListener true

            val fm = supportFragmentManager
            val currentFrag = fm.findFragmentByTag(currentTag)!!
            val newFrag = fm.findFragmentByTag(newTag)!!

            fm.beginTransaction()
                .hide(currentFrag)
                .show(newFrag)
                .setPrimaryNavigationFragment(newFrag)
                .commit()

            currentTag = newTag
            setNavListenerForTag(currentTag)
            true
        }
    }

    private fun setNavListenerForTag(tag: String) {
        val host = supportFragmentManager.findFragmentByTag(tag) as NavHostFragment
        val navController = host.navController

        currentNavController?.removeOnDestinationChangedListener(destinationChangedListener)
        currentNavController = navController
        navController.addOnDestinationChangedListener(destinationChangedListener)

        // 立即更新一次底部栏状态
        val currentDest = navController.currentDestination
        Log.d("MainActivity", "setNavListenerForTag: tag=$tag currentDest=${currentDest?.id}")
        setBottomNavVisible(currentDest == null || currentDest.id != R.id.loginFragment)
    }

    private fun findTabIndex(destination: NavDestination): Int? {
        val targetId = when {
            destination.id in tabMenuIds -> destination.id
            destination.parent?.id in tabMenuIds -> destination.parent?.id
            else -> null
        } ?: return null
        return tabMenuIds.indexOf(targetId).takeIf { it >= 0 }
    }

    private fun setBottomNavVisible(isVisible: Boolean) {
        viewBinding.bottomNav.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    private fun updateBottomNavSelection(menuId: Int) {
        suppressBottomSelection = true
        viewBinding.bottomNav.selectedItemId = menuId
        viewBinding.bottomNav.menu.findItem(menuId)?.isChecked = true
        suppressBottomSelection = false
    }

    companion object {
        private const val ROOT_TAG = "root_nav"
        private const val SELECTED_TAG = "selected_tag"
    }
}
