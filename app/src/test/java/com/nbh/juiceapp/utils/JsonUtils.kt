package com.nbh.juiceapp.utils

fun readJsonFromResources(filePath: String): String {
    val classLoader = Thread.currentThread().contextClassLoader
        ?.getResourceAsStream(filePath)
        ?: throw IllegalArgumentException("File not found: $filePath")
    return classLoader.bufferedReader().use { it.readText() }
}