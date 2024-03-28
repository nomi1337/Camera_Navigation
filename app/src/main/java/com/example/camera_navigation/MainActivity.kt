package com.example.camera_navigation
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.camera_navigation.ui.theme.Camera_NavigationTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
    MainScreen()
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier,
               context: Context = LocalContext.current){

    var bitmap by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if(it.resultCode == Activity.RESULT_OK){
            bitmap = it.data?.extras?.get("data") as Bitmap
        }else{
            Toast.makeText(context, "Image Capture Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){
        if(it){
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
            cameraLauncher.launch(intent)
        }else{
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text(text = "Main Screen") })},
        bottomBar = { BottomAppBar {
            Text("Bottom Bar")
        }}
    ) {
            paddingValues -> Column(modifier = Modifier.padding(paddingValues)) {
        Text(text = "Main Screen")
        Button(onClick = {

        }) {
            Text("Next Screen")
        }
        Button(onClick = {
            if(ContextCompat.checkSelfPermission(context,
                    android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, "Permission is already Granted", Toast.LENGTH_SHORT).show()
                cameraLauncher.launch(intent)

            }else{
                requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }) {
            Text("Open Camera")
        }
        bitmap?.let{
            Image(bitmap = it.asImageBitmap(), contentDescription = "", modifier = Modifier.fillMaxSize())
        }

    }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MainScreen()
}