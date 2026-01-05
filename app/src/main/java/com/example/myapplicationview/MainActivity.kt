package com.example.myapplicationview

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplicationview.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    // 使用 nav_graph.xml 中定义的子 navigation 的 id 作为每个 tab 的起始目的地 id
    private val navGraphIds: List<Int> = listOf(
        R.id.chat_graph,
        R.id.contact_graph,
        R.id.find_graph,
        R.id.me_graph
    )

    private val fragmentTags by lazy { navGraphIds.indices.map { i -> "bottomNav#${i}" } }
    private val ROOT_TAG = "root_nav"
    private var currentTag: String = ROOT_TAG // 初始显示 root host (login)
    private val SELECTED_TAG = "selected_tag"

    private var currentNavController: NavController? = null

    private val rootDestinationListener =
        NavController.OnDestinationChangedListener { controller, destination, _ ->
            // 如果 root NavController 导航到了 chat 的开始页面(nav_chat)，切换到 tab hosts
            if (destination.id == R.id.nav_chat || destination.parent?.id == R.id.chat_graph) {
                // 切换到 tab 模式并显示 chat tab
                switchToTabs(selectedIndex = 0)
            } else {
                // 在根导航（登录或其他页面）时隐藏底部栏
                viewBinding.bottomNav.visibility = View.GONE
            }
        }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
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

        // 初始创建 root NavHost 显示 loginFragment
        setupRootNavHost()
        setupBottomNav()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SELECTED_TAG, currentTag)
        super.onSaveInstanceState(outState)
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
                host = NavHostFragment.create(R.navigation.nav_graph)
                fm.beginTransaction()
                    .add(R.id.nav_host_fragment, host, tag)
                    .hide(host)
                    .commitNow()

                val navController = host.navController
                val inflater = navController.navInflater
                val graph = inflater.inflate(R.navigation.nav_graph)
                graph.setStartDestination(navGraphId)
                navController.graph = graph
            }
        }
    }

    private fun switchToTabs(selectedIndex: Int) {
        // 创建并显示 tab hosts（如果尚未创建），然后移除 root host
        setupNavHosts()

        val fm = supportFragmentManager
        // 隐藏并移除 root host
        val root = fm.findFragmentByTag(ROOT_TAG)
        if (root != null) {
            // remove the root fragment to avoid conflicting NavControllers
            fm.beginTransaction()
                .remove(root)
                .commitNow()
        }

        // 显示选中的 tab
        val newTag = fragmentTags[selectedIndex]
        val newFrag = fm.findFragmentByTag(newTag) as NavHostFragment
        fm.beginTransaction()
            .show(newFrag)
            .setPrimaryNavigationFragment(newFrag)
            .commitNow()

        currentTag = newTag
        setNavListenerForTag(currentTag)
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
        viewBinding.bottomNav.visibility = if (currentDest != null && currentDest.id == R.id.loginFragment) View.GONE else View.VISIBLE
    }
}