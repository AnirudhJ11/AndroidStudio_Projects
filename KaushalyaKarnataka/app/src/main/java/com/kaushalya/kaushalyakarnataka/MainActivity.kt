package com.kaushalya.kaushalyakarnataka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaushalya.kaushalyakarnataka.ui.KaushalyaApp
import com.kaushalya.kaushalyakarnataka.ui.theme.KaushalyaTheme
import com.kaushalya.kaushalyakarnataka.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KaushalyaTheme {
                val vm: AppViewModel = viewModel()
                KaushalyaApp(vm = vm)
            }
        }
    }
}