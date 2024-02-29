package fr.isen.berriri.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.isen.berriri.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class DishType {
    STARTER, MAIN, DESSERT;

    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface MenuInterface {
    fun dishPressed(dishType: DishType)
}

class MainActivity : ComponentActivity(), MenuInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //getString(R.string.menu_starter)
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SetupView(this)
                }
            }
        }
        Log.d("lifeCycle", "Home Activity - OnCreate")
    }

    override fun dishPressed(dishType: DishType) {
        val intent = Intent(this, MenuActivity::class.java)
        //intent.putExtra(MenuActivity.CATEGROY_EXTRA_KEY, dishType)
        startActivity(intent)
    }

    override fun onPause() {
        Log.d("lifeCycle", "Home Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Home Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Home Activity - onDestroy")
        super.onDestroy()
    }
}

@Composable
fun SetupView(menu: MenuInterface) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Blue)) { // Utilisez Box pour le centrage vertical
        Column(
            modifier = Modifier
                .align(Alignment.Center) // Centre la colonne dans la Box
                .wrapContentSize(), // La colonne prend la taille de son contenu
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomButton(type = DishType.STARTER, menu, Modifier.fillMaxWidth().height(60.dp))
            Spacer(modifier = Modifier.height(64.dp))
            CustomButton(type = DishType.MAIN, menu, Modifier.fillMaxWidth().height(60.dp))
            Spacer(modifier = Modifier.height(64.dp))
            CustomButton(type = DishType.DESSERT, menu, Modifier.fillMaxWidth().height(60.dp))
        }
    }
}


@Composable
fun CustomButton(type: DishType, menu: MenuInterface, modifier: Modifier = Modifier) {
    Button(
        onClick = { menu.dishPressed(type) },
        modifier = modifier
            .padding(horizontal = 16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White) // Remplacez 'YourChoice' par la couleur désirée
    ) {
        Text(type.title(), style = MaterialTheme.typography.bodyMedium, color = Color.Black)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        SetupView(MainActivity())
    }
}