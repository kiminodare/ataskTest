package com.aldera.atasktest

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.aldera.atasktest.databinding.ActivityMainBinding
import com.aldera.atasktest.BuildConfig
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            when (BuildConfig.FLAVOR_feature) {
                "builtInCamera" -> {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)

                    navController.navigate(R.id.CameraXFragment)
//                    setSupportActionBar(binding.toolbar)
                }
                "filesystem" -> {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)

                    navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main)

                    navController.navigate(R.id.FirstFragment)
                }
                else -> {
                    binding = ActivityMainBinding.inflate(layoutInflater)
                    setContentView(binding.root)
                }
            }
        } else {
            Log.d("MainActivity", "savedInstanceState is not null")
        }
//        val navController = findNavController(R.id.nav_host_fragment_content_main)
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)


//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAnchorView(R.id.fab)
//                .setAction("Action", null).show()
//        }
        Log.d("MainActivity", BuildConfig.FLAVOR_feature)
        checkPermission()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun checkPermission() {
        val permissions = listOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )

        val permissionRequest = mutableListOf<String>()
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                permissionRequest.add(permission)
            }
        }

        if (permissionRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionRequest.toTypedArray(), 0)
        } else {
            Dexter.withContext(this)
                .withPermissions(permissionRequest)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
                        if (p0!!.areAllPermissionsGranted()) {
                            Snackbar.make(
                                binding.root,
                                "All permissions are granted",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        if (p0.isAnyPermissionPermanentlyDenied) {
                            Snackbar.make(
                                binding.root,
                                "Some permissions are permanently denied",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        p0: MutableList<PermissionRequest>?,
                        p1: PermissionToken?
                    ) {
                        p1!!.continuePermissionRequest()
                    }

                }).check()
        }
    }

}