package com.hw.hwauthenticate.Bean

import android.os.Parcel
import android.os.Parcelable
import android.telephony.cdma.CdmaCellLocation

/**
 * listView data bean
 */
class ToDoBean(var id: Long, var read: Boolean = false, var date: String?,
               var details: String?, var location_x: Long, var location_y: Long) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
            parcel.readLong(),
            parcel.readLong()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeByte(if (read) 1 else 0)
        parcel.writeString(date)
        parcel.writeString(details)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ToDoBean> {
        override fun createFromParcel(parcel: Parcel): ToDoBean {
            return ToDoBean(parcel)
        }

        override fun newArray(size: Int): Array<ToDoBean?> {
            return arrayOfNulls(size)
        }
    }


}