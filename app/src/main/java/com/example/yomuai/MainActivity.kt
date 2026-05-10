package com.example.yomuai

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.rememberAsyncImagePainter
import com.example.yomuai.ui.theme.YomuAITheme
import kotlinx.coroutines.launch
import java.util.Base64

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YomuAITheme(darkTheme = true) { // Enforce Dark Theme
                MainScreen()
            }
        }
    }
}

class MangaViewModel : ViewModel() {
    private val scraper = Scraper()
    val mangaList = mutableStateListOf<Manga>()

    init {
        fetchManga()
    }

    private fun fetchManga() {
        viewModelScope.launch {
            val fetchedManga = scraper.fetchManga("https://www.asurascans.com/manga")
            mangaList.addAll(fetchedManga)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MangaViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedManga by remember { mutableStateOf<Manga?>(null) }

    if (selectedManga != null) {
        // Dummy chapter URLs for now
        val chapterImageUrls = listOf(
            "https://i.imgur.com/8zU45r0.jpeg",
            "https://i.imgur.com/uG9t3X0.jpeg",
            "https://i.imgur.com/0AnNx4C.jpeg"
        )
        ReaderScreen(chapterImageUrls = chapterImageUrls)
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("YomuAI Gen-2") },
                    actions = {
                        // Simple Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            label = { Text("Search") },
                            modifier = Modifier.padding(end = 8.dp)
                        )
                    }
                )
            },
            bottomBar = {
                BottomNavigationBar()
            }
        ) { innerPadding ->
            LibraryGrid(
                mangaList = viewModel.mangaList,
                modifier = Modifier.padding(innerPadding),
                onMangaClick = { manga ->
                    selectedManga = manga
                }
            )
        }
    }
}

@Composable
fun LibraryGrid(mangaList: List<Manga>, modifier: Modifier = Modifier, onMangaClick: (Manga) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = modifier.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(mangaList) { manga ->
            MangaCard(manga = manga, onClick = { onMangaClick(manga) })
        }
    }
}

@Composable
fun MangaCard(manga: Manga, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .clickable(onClick = onClick),
    ) {
        Column {
            if (manga.isBase64) {
                val imageBytes = Base64.getDecoder().decode(manga.coverUrl.substringAfter(','))
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = manga.title,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(manga.coverUrl),
                    contentDescription = manga.title,
                    modifier = Modifier
                        .height(180.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = manga.title,
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Library", "Search", "Settings")
    val icons = listOf(Icons.Filled.LibraryBooks, Icons.Filled.Search, Icons.Filled.Settings)

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item) },
                selected = selectedItem == index,
                onClick = { selectedItem = index }
            )
        }
    }
}


fun getDummyManga(): List<Manga> {
    return listOf(
        Manga(1, "Attack on Titan", "", false),
        Manga(2, "One Piece", "", false),
        Manga(3, "Naruto", "", false),
        Manga(4, "My Hero Academia", "", false),
        Manga(5, "Jujutsu Kaisen", "", false),
        Manga(6, "Demon Slayer", "", false),
        Manga(7, "Bleach", "", false),
        Manga(8, "Fullmetal Alchemist", "", false),
        Manga(9, "Dragon Ball Z", "", false),
        Manga(10, "Death Note", "", false)
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YomuAITheme(darkTheme = true) {
        MainScreen()
    }
}
