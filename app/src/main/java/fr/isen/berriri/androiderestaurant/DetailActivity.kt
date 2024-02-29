package fr.isen.berriri.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.berriri.androiderestaurant.basket.Basket
import fr.isen.berriri.androiderestaurant.basket.BasketActivity
import fr.isen.berriri.androiderestaurant.network.Dish
import fr.isen.berriri.androiderestaurant.ui.theme.AndroidERestaurantTheme
import kotlin.math.max

class DetailActivity : ComponentActivity() {
    @OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dish = intent.getSerializableExtra(DISH_EXTRA_KEY) as? Dish
        setContent {
            val context = LocalContext.current
            val count = remember {
                mutableIntStateOf(1)
            }
            val ingredient = dish?.ingredients?.map { it.name }?.joinToString(", ") ?: ""
            val pagerState = rememberPagerState(pageCount = {
                dish?.images?.count() ?: 0
            })

            val buttonColors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Fond blanc pour les boutons
                contentColor = Color.Black // Texte noir pour les boutons
            )
            Box(modifier = Modifier.fillMaxSize().background(Color.Blue)){
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    TopAppBar({
                        Text(
                            dish?.name ?: "",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    })
                    HorizontalPager(state = pagerState) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(dish?.images?.get(it))
                                .build(),
                            null,
                            placeholder = painterResource(R.drawable.ic_launcher_foreground),
                            error = painterResource(R.drawable.ic_launcher_foreground),
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth(0.8f)
                                .padding(16.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = ingredient,
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Spacer(Modifier.weight(1f))
                        OutlinedButton(onClick = {
                            count.value = max(1, count.value - 1)
                        }) {
                            Text("-")
                        }
                        Text(count.value.toString())
                        OutlinedButton(onClick = {
                            count.value = count.value + 1
                        }) {
                            Text("+")
                        }
                        Spacer(Modifier.weight(1f))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    )
                    {
                        Button(onClick = {
                            if (dish != null) {
                                Basket.current(context).add(dish, count.value, context)
                            }
                        },
                            colors = buttonColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Text("Commander")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            val intent = Intent(context, BasketActivity::class.java)
                            context.startActivity(intent)
                        },
                            colors = buttonColors,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp)
                        ) {
                            Text("Voir mon panier")
                        }
                    }
                }
            }
        }
        Log.d("lifeCycle", "Detail Activity - OnCreate")

    }

    companion object {
        val DISH_EXTRA_KEY = "DISH_EXTRA_KEY"
    }
}