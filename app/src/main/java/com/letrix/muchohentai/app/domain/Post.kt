package com.letrix.muchohentai.app.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlin.collections.List as CollectionsList

data class Post(
    @SerializedName("id") val postId: Int,
    val series: String,
    @SerializedName("series_id") val seriesId: String,
    val episode: Int?,
    val type: String,
    val uncensored: Boolean,
    val views: Int,
    val thumbnail: String,
    @SerializedName("audio") val audioLanguage: String,
    @SerializedName("subtitles") val subtitleLanguage: String,
    val cover: String?,
    val tags: CollectionsList<String>,
    @SerializedName("url") val postUrl: String
) : Parcelable {

    data class List(
        val title: String,
        val list: CollectionsList<Post>
    )

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        parcel.readByte() != 0.toByte(),
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postId)
        parcel.writeString(series)
        parcel.writeString(seriesId)
        parcel.writeValue(episode)
        parcel.writeString(type)
        parcel.writeByte(if (uncensored) 1 else 0)
        parcel.writeInt(views)
        parcel.writeString(thumbnail)
        parcel.writeString(audioLanguage)
        parcel.writeString(subtitleLanguage)
        parcel.writeString(cover)
        parcel.writeStringList(tags)
        parcel.writeString(postUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Post> {
        override fun createFromParcel(parcel: Parcel): Post {
            return Post(parcel)
        }

        override fun newArray(size: Int): Array<Post?> {
            return arrayOfNulls(size)
        }
    }
}