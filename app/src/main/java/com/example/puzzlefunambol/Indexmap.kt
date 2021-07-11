package com.example.puzzlefunambol

import android.graphics.Bitmap

//La classe Indexmap ha lo scopo di contenere un frammento di immagine e la sua posizione originale, così da poter recuperare la sequenza corretta indipendentemente dagli spostamenti
class Indexmap {
    var img: Bitmap
    var index: Int

    constructor(bitmap: Bitmap,i: Int){
        img = bitmap
        index = i
    }

    fun getIndice(): Int {
        return index
    }

    fun getBitmap(): Bitmap{
        return img
    }

}