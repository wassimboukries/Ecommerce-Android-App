# Ecommerce-Android-App

This is an Andorid E-commerce application. The apk file is the root of the project.
Kotlin is the main langage of the app.

## Usage

You can use this app to view products from the BEST BUY Api. Products are grouped into categories.

You can view the products of each categorie. Each product has a title, a rating (and the numbers of customers who rated the product), an image and it's price in Euros (€).

You have the option to add the product to youre favrite products List (or remove it if it's already there). You can view this list anytime you want through the item in the menu (the three points in top-right corner of the screen).

You have also the option to view the original page of a product (BEST BUY webpage).


## Tech

In this project we used these librairie :

- **OkHttp** as an HTTP client to fetch data from the Best Buy API (https://github.com/square/okhttp)
- **Glide** as an image loading tool (https://github.com/bumptech/glide)
- **Room** as a persitance librairy. It provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.(https://developer.android.com/jetpack/androidx/releases/room)


The application is single activity based. We navigate the multiple fragments (categories, products, productDetail) through the navigation graph.
