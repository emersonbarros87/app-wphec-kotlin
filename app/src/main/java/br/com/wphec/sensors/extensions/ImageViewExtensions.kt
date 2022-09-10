package br.com.wphec.sensors.extensions

import android.widget.ImageView
import br.com.wphec.sensors.R
import coil.load

fun ImageView.loadImage(
    url: String? = null,
    fallback: Int = R.drawable.imagem_padrao
){
    load(url) {
        fallback(fallback)
        error(R.drawable.erro)
        placeholder(R.drawable.placeholder)
    }
}