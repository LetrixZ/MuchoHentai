package com.letrix.muchohentai.app.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Episode(
    val postId: Int,
    val series: String,
    val seriesId: String,
    val episode: Int?,
    val summary: String,
    val type: String,
    val views: Int,
    val audioLanguage: String,
    val subtitleLanguage: String,
    val cover: String?,
    val tags: List<String>,
    val streamUrl: String,
    val subUrl: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createStringArrayList()!!,
        parcel.readString()!!,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(postId)
        parcel.writeString(series)
        parcel.writeString(seriesId)
        parcel.writeValue(episode)
        parcel.writeString(summary)
        parcel.writeString(type)
        parcel.writeInt(views)
        parcel.writeString(audioLanguage)
        parcel.writeString(subtitleLanguage)
        parcel.writeString(cover)
        parcel.writeStringList(tags)
        parcel.writeString(streamUrl)
        parcel.writeString(subUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Episode> {
        override fun createFromParcel(parcel: Parcel): Episode {
            return Episode(parcel)
        }

        override fun newArray(size: Int): Array<Episode?> {
            return arrayOfNulls(size)
        }
    }
}