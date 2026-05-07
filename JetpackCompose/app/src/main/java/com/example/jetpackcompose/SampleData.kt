package com.example.jetpackcompose

data class Message(val author: String, val body: String)

object SampleData {
    val conversationSample = listOf(
        Message("Lexi", "Test...Test...Test..."),
        Message("Lexi", "List of Android versions:\nAndroid KitKat (API 19)\nAndroid Lollipop (API 21)\nAndroid Marshmallow (API 23)\nAndroid Nougat (API 24)\nAndroid Oreo (API 26)\nAndroid Pie (API 28)\nAndroid 10 (API 29)\nAndroid 11 (API 30)\nAndroid 12 (API 31)"),
        Message("Lexi", "I think Kotlin is my favorite programming language.\nIt's so much fun!"),
        Message("Lexi", "Searching for alternatives to XML layouts..."),
        Message("Lexi", "Hey, take a look at Jetpack Compose, it's great!\nIt's the Android's modern toolkit for building native UI.\nIt simplifies and accelerates UI development on Android.\nLess code, powerful tools, and intuitive Kotlin APIs :)"),
        Message("Lexi", "It's available from API 21+"),
        Message("Lexi", "Writing Kotlin for UI seems so natural, Compose where have you been all my life?")
    )
}