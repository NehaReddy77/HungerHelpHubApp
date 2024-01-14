package mypackage.h2hub.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

class ParcelableGeoPoint(val latitude: Double, val longitude: Double) : Parcelable {

    constructor(geoPoint: GeoPoint) : this(geoPoint.latitude, geoPoint.longitude)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableGeoPoint> {
        override fun createFromParcel(parcel: Parcel): ParcelableGeoPoint {
            val latitude = parcel.readDouble()
            val longitude = parcel.readDouble()
            return ParcelableGeoPoint(latitude, longitude)
        }

        override fun newArray(size: Int): Array<ParcelableGeoPoint?> {
            return arrayOfNulls(size)
        }
    }
}
