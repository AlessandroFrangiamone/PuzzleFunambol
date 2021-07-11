package com.example.puzzlefunambol

import android.content.Context
import android.view.*
import android.widget.BaseAdapter
import android.widget.ImageView

//Adapter per visualizzare gli elementi della GridView
class ImageAdapter : BaseAdapter {
    var imageList = ArrayList<Indexmap>()
    var context: Context? = null

    constructor(context: Context, imageList: ArrayList<Indexmap>) : super () {
        this.context = context
        this.imageList = imageList
    }

    //Metodo che effettua lo swap tra due elementi della GridView
    fun swapImage(x: Int, y: Int){
        var temp: Indexmap
        temp=imageList[x]
        imageList[x]=imageList[y]
        imageList[y]=temp
    }

    override fun getCount(): Int {
        return imageList.size
    }

    override fun getItem(position: Int): Indexmap {
        return imageList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val image = this.imageList[position].getBitmap()

        val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val imageView = inflator.inflate(R.layout.image_entry, parent, false)
        imageView.findViewById<ImageView>(R.id.imgPuzzle).setImageBitmap(image)

        return imageView
    }

    //Metodo che mischia i frammenti delle imamgini
    fun shuffleImage(){
        imageList.shuffle()
    }

    //Metodo che verifica la correttezza della posizione attuale rispetto alla posizione reale
    fun allCorrectPosition(): Boolean{
        var counter = 0
        for(i in imageList){
            if(i.getIndice() != counter)
                return false
            counter++
        }
        return true
    }

}