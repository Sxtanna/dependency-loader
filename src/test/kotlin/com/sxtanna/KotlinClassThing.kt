package com.sxtanna

class KotlinClassThing {

	private val listOfWords = mutableListOf<String>()

	init {
		println("Omg, This is Kotlin... wow")

		listOfWords.addAll(arrayOf("Hello", "World", "Goodbye", "World"))
	}

}