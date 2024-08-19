package com.example.client_for_reddit.Model

import android.os.Parcel
import android.os.Parcelable

data class RedditPost(
    val author: String?,
    val description: String?,
    val date: Long,
    val img: String?,
    val countComments: Int,
    val isVideo: Boolean,
    val urlVideo: String?,
    val thumbnail: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        author = parcel.readString(),
        description = parcel.readString(),
        date = parcel.readLong(),
        img = parcel.readString(),
        countComments = parcel.readInt(),
        isVideo = parcel.readByte() != 0.toByte(),
        urlVideo = parcel.readString(),
        thumbnail = parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(author)
        parcel.writeString(description)
        parcel.writeLong(date)
        parcel.writeString(img)
        parcel.writeInt(countComments)
        parcel.writeByte(if (isVideo) 1 else 0)
        parcel.writeString(urlVideo)
        parcel.writeString(thumbnail)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RedditPost> {
        override fun createFromParcel(parcel: Parcel): RedditPost {
            return RedditPost(parcel)
        }

        override fun newArray(size: Int): Array<RedditPost?> {
            return arrayOfNulls(size)
        }
    }
}
