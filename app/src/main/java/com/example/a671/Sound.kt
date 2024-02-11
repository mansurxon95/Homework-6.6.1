package com.example.a671

import android.net.Uri
import java.io.Serializable

class Sound : Serializable {
    var id:Int?=null
    var image:Uri?=null
    var title: String?=null
    var artist: String?=null
    var path: String?=null

    constructor(id:Int, image: Uri, title: String?, artist: String?, path: String?) {
        this.id = id
        this.image = image
        this.title = title
        this.artist = artist
        this.path = path
    }

    constructor()
}