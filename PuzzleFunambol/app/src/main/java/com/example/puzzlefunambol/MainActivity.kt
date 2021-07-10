/*
    -   Autore: Alessandro Frangiamone
    -   Data ultima verisone: 10/07/2021

    -   Descrizione: Esercizio per Funambol, consiste nel prendere un'immagine e spezzarla in una griglia 3X3, mischiare i pezzi e utilizzando il drag and drop
                     ricomporla nello stato originale.

*/
package com.example.puzzlefunambol

import android.content.ClipData
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.View.DragShadowBuilder
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition


class MainActivity : AppCompatActivity(), View.OnDragListener, AdapterView.OnItemLongClickListener {

    var BSelectIMG: Button? = null

    var ViewIMG: ImageView? = null

    var grid: GridView? = null

    var imageUri: Uri? = null


    val bitmapArray = ArrayList<Indexmap>()


    var adapterIMG: ImageAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        BSelectIMG = findViewById(R.id.selectIMG)
        ViewIMG = findViewById(R.id.PreviewImage)
        grid = findViewById(R.id.GridImage)

        //Al click del bottone apre la galleria, in cui poi è possibile selezionare una qualsiasi immagine
        BSelectIMG?.setOnClickListener(View.OnClickListener {
            //pulisco l'arrey di bitmap contenente i frammenti dell'immagine precedente
            bitmapArray.clear()
            pickImage()
        })

        //Setto longclicklistener e draglistener sulla GridView
        grid?.setOnItemLongClickListener(this);
        grid?.setOnDragListener(this)

        //Inizialmente di default carico l'immagine fornita al link del PDF
        loadInitialIMG()
    }

    //Carico l'immagine iniziale fornita al link del PDF
    fun loadInitialIMG(){
        //Utilizzo Glide per aprire comodamente l'immagine dall'URL e convertirla in Bitmap
        Glide.with(this)
            .asBitmap()
            .load("https://picsum.photos/1024")
            .into(object: CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    //Quando la risorsa è pronta (immagine in questo caso) richiamo il metodo puzzleIMG che mi spezza l'img in blocchi da 9 e li mischia
                    puzzleIMG(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    TODO("Not yet implemented")
                }
            })
    }

    //Apro la galleria di immagini
    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1000)
    }

    //Viene eseguito appena dopo aver scelto l'img dalla galleria, la converto da uri a bitmap e la spezzo in 9 parti e mischio con in metodo puzzleIMG
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 1000) {
            imageUri = data?.data
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            puzzleIMG(bitmap)
        }
    }

    //Metodo che mi spezza l'img in 9 parti uguali (a griglia 3X3), mischia i pezzi e li inserisce nella GridView
    private fun puzzleIMG(bitmap: Bitmap){
        val bMapScaled : Bitmap
        bMapScaled = Bitmap.createScaledBitmap(bitmap, 1000, 1500, true)

        //Scompongo l'immagine in una griglia di 9 elementi (3X3)
        var un_terzo_w = bMapScaled.width / 3
        var due_terzi_w = (bMapScaled.width / 3)*2
        var un_terzo_h = bMapScaled.height / 3
        var due_terzi_h = (bMapScaled.height / 3)*2

        //Variabile che mi serve per tenere memorizzato l'ordine iniziale delle indexmap (Bitmap+indice corretto iniziale)
        var i= 0

        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, 0, 0, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, un_terzo_w, 0, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, due_terzi_w, 0, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, 0, un_terzo_h, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, un_terzo_w, un_terzo_h, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, due_terzi_w, un_terzo_h, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, 0, due_terzi_h, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, un_terzo_w, due_terzi_h, un_terzo_w, un_terzo_h),i++))
        bitmapArray.add(Indexmap(Bitmap.createBitmap(bMapScaled, due_terzi_w, due_terzi_h, un_terzo_w, un_terzo_h),i++))

        //Creo l'image adapter con l'array di imagemap
        adapterIMG = ImageAdapter(this, bitmapArray)
        //Mischio le immagini
        adapterIMG!!.shuffleImage()
        grid?.adapter = adapterIMG
    }

    //Metodo evocato all'evento di drag and drop
    override fun onDrag(v: View?, event: DragEvent?): Boolean {
        when (event!!.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                //Non implementato
                Log.d("ELEMENTO", " DRAG")
                return true;
            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                //Non implementato
                //Log.d("ELEMENTO", " ENTERED")
                return true;
            }
            DragEvent.ACTION_DRAG_EXITED -> {
                //Non implementato
                //Log.d("ELEMENTO", " EXITED")
                return true;
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                //Non implementato
                //Log.d("ELEMENTO", " LOCATION: X -> "+event.x+", Y -> "+event.y)
                return true;
            }
            //Al drop voglio che se i frammenti dell'immagine non sono nella posizione corretto, si swappino tra loro
            DragEvent.ACTION_DROP -> {
                Log.d("ELEMENTO", " DROP")
                Log.d("ELEMENTO"," -> "+event.clipData.getItemAt(0).text.toString().toInt())

                var pos = event.clipData.getItemAt(0).text.toString().toInt()

                //Controllo che l'img non sia in posizone corretta, altrimenti non eseguo lo swap
                if(pos != adapterIMG?.getItem(pos)?.getIndice()){
                    var WIDTH = v!!.width
                    var HEIGHT= v!!.height
                    var pointer_w = event.x
                    var pointer_h = event.y

                    var pos_target: Int

                    //Faccio il check della posizione target a livello di dimensione immagini, per poi swapparla

                    //CHECK riga 1
                    if(pointer_h <= (HEIGHT/3)){
                        if(pointer_w <= (WIDTH/3))
                            pos_target = 0
                        else
                            if(pointer_w >= ((WIDTH/3)*2)) {
                                pos_target = 2
                            }else
                                pos_target = 1
                    }else{
                        //CHECK riga 3
                        if(pointer_h >= ((HEIGHT/3)*2)) {
                            if (pointer_w <= (WIDTH / 3))
                                pos_target = 6
                            else
                                if (pointer_w >= ((WIDTH / 3) * 2)) {
                                    pos_target = 8
                                } else
                                    pos_target = 7
                        }else{
                            //CHECK riga 2
                            if (pointer_w <= (WIDTH / 3))
                                pos_target = 3
                            else
                                if (pointer_w >= ((WIDTH / 3) * 2)) {
                                    pos_target = 5
                                } else
                                    pos_target = 4
                        }
                    }

                    Log.d("ELEMENTO"," Pos Target -> $pos_target")
                    Log.d("ELEMENTO"," View size -> "+v!!.width+" - "+v!!.height)
                    Log.d("ELEMENTO"," Posizione: X -> "+event.x+", Y -> "+event.y)

                    //Swappo l'immagine
                    adapterIMG!!.swapImage(event.clipData.getItemAt(0).text.toString().toInt(),pos_target)
                    //Notifico il cambiamento
                    adapterIMG!!.notifyDataSetChanged()

                    //Eseguo un check se tutte le celle sono in posizone corretta, in caso affermativo verrà scritto un toast su schermo
                    if(adapterIMG!!.allCorrectPosition()){
                        Toast.makeText(this, "Complimenti!!\nHai completato l'immagine", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(this, "L'elemento è già in posizione corretta", Toast.LENGTH_SHORT).show()
                }
                return true;
            }
            else ->
                return false
        }
    }

    //Quando eseguo un longclick su una cella si avvi ail drag and drop, come clip data invio al drag la posizione dell'elemento iniziale
    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {
        val clipText = "POSIZIONE"
        val item = ClipData.Item("$position")
        val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
        val data = ClipData(clipText, mimeTypes, item)

        val dragShadowBuilder = View.DragShadowBuilder(view)
        view!!.startDragAndDrop(data, dragShadowBuilder, view, 0)

        //view.visibility = View.INVISIBLE

        return true
    }
}