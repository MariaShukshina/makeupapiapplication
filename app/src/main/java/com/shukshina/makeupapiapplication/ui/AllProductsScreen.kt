package com.shukshina.makeupapiapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.shukshina.makeupapiapplication.response.ProductsList
import com.shukshina.makeupapiapplication.utils.Constants

@Composable
fun AllProductsScreen(navController: NavHostController, viewModel: MainActivityViewModel,
                      productsList: ProductsList?, isConnected: Boolean) {
    val searchQuery = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {

        AllProductsTopSection(searchQuery, viewModel) {
            viewModel.getProductsByProductType("lipstick")
        }

        if (isConnected) {
            if (productsList != null) {
                val searchedText = searchQuery.value
                if(!productsList.isEmpty()) {
                    val filteredProducts = if (searchedText.isEmpty()) {
                        productsList
                    } else {
                        val resultList = ProductsList()
                            for (item in productsList) {
                                if (item.name?.contains(searchedText, ignoreCase = true) == true
                                    || item.brand?.contains(searchedText, ignoreCase = true) == true
                                    || item.category?.contains(searchedText, ignoreCase = true) == true
                                ) {
                                    resultList.add(item)
                                }
                            }
                        resultList
                    }
                    if (filteredProducts.isNotEmpty()) {
                        ProductsListSection(navController, filteredProducts, viewModel = viewModel)
                    } else {
                        InformationForUser("No products found")
                    }
                } else {
                    Loading()
                }
            } else {
                Loading()
            }
        } else {
            InformationForUser("Please check internet connection")
        }
    }
}

@Composable
fun Loading() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = Color(249,116,167))
    }
}

@Composable
fun InformationForUser(text: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = text, color = MaterialTheme.colors.error)
    }
}

@Composable
fun AllProductsTopSection(searchQuery: MutableState<String>, viewModel: MainActivityViewModel,
                          onRefreshClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedItem by remember { mutableStateOf(Constants.productTypes[0]) }

    Row {
        SearchBar(
            hint = "Search by name, brand or category",
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(10.dp),
            searchQuery = searchQuery,
        )
        Spacer(modifier = Modifier.width(4.dp))

        IconButton(
            onClick = onRefreshClick,
            modifier = Modifier
                .fillMaxWidth(0.4f)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = "Refresh")
        }
        Spacer(modifier = Modifier.width(4.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .wrapContentSize(Alignment.TopStart)
        ) {
            IconButton(onClick = { expanded = true }) {
                Icon(Icons.Default.Menu, contentDescription = "Dropdown")
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                offset = DpOffset(0.dp, 20.dp)
            ) {
                Constants.productTypes.forEach { type ->
                    DropdownMenuItem(onClick = {
                        selectedItem = type
                        expanded = false
                        when (type.id) {
                            "All products" -> viewModel.getAllProducts()
                            else -> viewModel.getProductsByProductType(type.id)
                        }
                    }) {
                        Text(text = type.name)
                    }
                }
            }
        }
    }
}