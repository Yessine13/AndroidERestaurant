package fr.isen.berriri.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.berriri.androiderestaurant.network.Category
import fr.isen.berriri.androiderestaurant.network.Dish
import fr.isen.berriri.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val type = (intent.getSerializableExtra(CATEGROY_EXTRA_KEY) as? DishType) ?: DishType.STARTER

        setContent {
            MenuView(type)
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    override fun onPause() {
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Menu Activity - onDestroy")
        super.onDestroy()
    }

    companion object {
        val CATEGROY_EXTRA_KEY = "CATEGROY_EXTRA_KEY"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(type: DishType) {
    val category = remember { mutableStateOf<Category?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize() // La Box remplit toute la taille disponible
            .background(Color.Blue) // Définit la couleur de fond de la Box
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopAppBar({
                Text(text = type.title(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)
            })
            LazyColumn(
                modifier = Modifier.padding(top = 8.dp), // Ajoutez un padding en haut si nécessaire
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                category.value?.let {
                    items(it.items) {
                        DishRow(it)
                    }
                }
            }
        }
    }
    PostData(type, category)
}


@Composable fun DishRow(dish: Dish) {
    val context = LocalContext.current
    Card(colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(0.8f)
            .clickable {
                val intent = Intent(context, DetailActivity::class.java)
                //intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                context.startActivity(intent)
            }
    ) {
        Row(Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(dish.images.first())
                    .build(),
                null,
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(10))
                    .padding(8.dp)
            )
            Text(dish.name,
                Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(8.dp),
                color = Color.Black
            )
            Spacer(Modifier.weight(1f))
            Text("${dish.prices.first().price} €",
                Modifier.align(alignment = Alignment.CenterVertically),
                color = Color.Black)
        }
    }
}

@Composable
fun PostData(type: DishType, category: MutableState<Category?>) {
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(
        Request.Method.POST,
        NetworkConstants.URL,
        params,
        { response ->
            Log.d("request", response.toString(2))
            val result =
                GsonBuilder().create().fromJson(response.toString(), MenuResult::class.java)
            val filteredResult = result.data.first { category -> category.name == currentCategory }
            category.value = filteredResult
        },
        {
            Log.e("request", it.toString())
        }
    )

    queue.add(request)
}

