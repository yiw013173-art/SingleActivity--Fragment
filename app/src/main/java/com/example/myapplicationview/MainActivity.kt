package com.example.myapplicationview

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplicationview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    // 使用 nav_graph.xml 中定义的子 navigation 的 id 作为每个 tab 的起始目的地 id（用于 menu id 映射）
    private val navGraphIds: List<Int> = listOf(
        R.id.chat_graph,
        R.id.contact_graph,
        R.id.find_graph,
        R.id.me_graph
    )

    // 明确定义每个子 navigation 对应的 startDestination（从 nav_graph.xml 中提取）
    private val navGraphStartDestinations: Map<Int, Int> = mapOf(
        R.id.chat_graph to R.id.nav_chat,
        R.id.contact_graph to R.id.nav_contact,
        R.id.find_graph to R.id.nav_find,
        R.id.me_graph to R.id.nav_me
    )

    private val fragmentTags by lazy { navGraphIds.indices.map { i -> "bottomNav#${i}" } }
    private val ROOT_TAG = "root_nav"
    private var currentTag: String = ROOT_TAG // 初始显示 root host (login)
    private val SELECTED_TAG = "selected_tag"

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
            val targetGraphId: Int? = when {
                // 如果 destination 本身就是某个子导航的根 id（例如 nav_chat），则尝试从 parent 中获取 graph id
                destination.id in navGraphIds -> destination.id
                // 如果 destination 的 parent 是我们其中的一个子 navigation，则取 parent id
                destination.parent?.id in navGraphIds -> destination.parent?.id
                else -> null
            }

            if (targetGraphId != null) {
                // 根据 targetGraphId 选择要显示的 tab index
                val found = navGraphIds.indexOf(targetGraphId)
                val index = if (found >= 0) found else 0
                Log.d("MainActivity", "rootDestinationListener: switching to tabs index=$index")
                switchToTabs(selectedIndex = index)
            } else {
                // 在根导航（登录或其他页面）时隐藏底部栏
                viewBinding.bottomNav.visibility = View.GONE
            }
        }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "destinationChangedListener: destination=${destination.id}")
            // 在 tab NavController 中，当目的地是 loginFragment 时隐藏 bottomBar（通常不会发生）
            viewBinding.bottomNav.visibility = if (destination.id == R.id.loginFragment) View.GONE else View.VISIBLE
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
        viewBinding.bottomNav.visibility = View.GONE
        currentNavController = navController
    }

    private fun setupNavHosts() {
        val fm = supportFragmentManager

        // 仅创建 hosts，不改变显示状态
        navGraphIds.forEachIndexed { index, navGraphId ->
            val tag = fragmentTags[index]
            var host = fm.findFragmentByTag(tag) as NavHostFragment?
            if (host == null) {
                // 第一步：使用根图创建 NavHostFragment
                host = NavHostFragment.create(R.navigation.nav_graph)

                fm.beginTransaction()
                    .add(R.id.nav_host_fragment, host, tag)
                    .hide(host)
                    .commitNow()

                // 第二步：在 Fragment 完全创建后，根据导航图 ID 的类型进行处理
                val navController = host.navController
                val inflater = navController.navInflater
                val rootGraph = inflater.inflate(R.navigation.nav_graph)

                // 检查当前 navGraphId 是否是导航图节点还是普通 Fragment
                val navNode = rootGraph.findNode(navGraphId)

                if (navNode is NavGraph) {
                    // 这是一个嵌套导航图（如 chat_graph），需要设置为子图
                    try {
                        navController.graph = navNode
                        Log.d("MainActivity", "setupNavHosts: created host tag=$tag using NavGraph=$navGraphId")
                    } catch (e: IndexOutOfBoundsException) {
                        Log.e("MainActivity", "setupNavHosts: IndexOutOfBoundsException when setting NavGraph for $navGraphId", e)
                        try {
                            val rootGraphFallback = inflater.inflate(R.navigation.nav_graph)
                            navController.graph = rootGraphFallback
                        } catch (fallbackError: Exception) {
                            Log.e("MainActivity", "setupNavHosts: fallback to root graph failed", fallbackError)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "setupNavHosts: failed to set NavGraph for $navGraphId", e)
                        try {
                            val rootGraphFallback = inflater.inflate(R.navigation.nav_graph)
                            navController.graph = rootGraphFallback
                        } catch (fallbackError: Exception) {
                            Log.e("MainActivity", "setupNavHosts: even fallback failed", fallbackError)
                        }
                    }
                } else {
                    // 这是一个普通 Fragment（如 contact_graph、find_graph、me_graph）
                    // 保持使用根图，NavController 会自动导航到指定的 Fragment
                    try {
                        // 设置根图，让 NavController 初始化
                        navController.graph = rootGraph
                        // 然后导航到指定的 Fragment
                        navController.navigate(navGraphId)
                        Log.d("MainActivity", "setupNavHosts: created host tag=$tag and navigated to Fragment=$navGraphId")
                    } catch (e: Exception) {
                        Log.e("MainActivity", "setupNavHosts: failed to navigate to Fragment $navGraphId", e)
                        try {
                            val rootGraphFallback = inflater.inflate(R.navigation.nav_graph)
                            navController.graph = rootGraphFallback
                        } catch (fallbackError: Exception) {
                            Log.e("MainActivity", "setupNavHosts: fallback failed", fallbackError)
                        }
                    }
                }
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
        viewBinding.bottomNav.visibility = View.VISIBLE
        val menuId = navGraphIds.getOrNull(selectedIndex) ?: navGraphIds.first()
        // 抑制监听器，程序性设置选中项
        suppressBottomSelection = true
        viewBinding.bottomNav.selectedItemId = menuId
        // 额外设置 menu item checked，确保高亮生效
        viewBinding.bottomNav.menu.findItem(menuId)?.isChecked = true
        suppressBottomSelection = false
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
        // menu item id 与 navGraphIds 保持一致（bottom_nav_menu 中使用了 chat_graph 等 id）
        val idToIndex: Map<Int, Int> = mapOf(
            R.id.chat_graph to 0,
            R.id.contact_graph to 1,
            R.id.find_graph to 2,
            R.id.me_graph to 3
        )

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
        viewBinding.bottomNav.visibility = if (currentDest != null && currentDest.id == R.id.loginFragment) View.GONE else View.VISIBLE
    }
}