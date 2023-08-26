package fr97.movieinfo.data.api


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class MovieDetailsResponse(
    val id: Int = 0,
    val title: String = "",
    @SerializedName("backdrop_path")
    val backdropPath: String? = "",
    val genres: List<Genre> = listOf(),
    val homepage: String? = "",
    @SerializedName("original_language")
    val originalLanguage: String = "",
    @SerializedName("original_title")
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    @SerializedName("poster_path")
    val posterPath: String = "",
    @SerializedName("release_date")
    val releaseDate: String = "",
    val runtime: Int = 0,
    val status: String = "",
    val video: Boolean = false,
    @SerializedName("vote_average")
    val voteAverage: Double = 0.0,
    @SerializedName("vote_count")
    val voteCount: Int = 0,
    val credits: Credits
)


data class Credits(val crew: List<CrewMember>, val cast: List<CastMember>)

data class CrewMember(val name: String, val job: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(job)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CrewMember> {
        override fun createFromParcel(parcel: Parcel): CrewMember {
            return CrewMember(parcel)
        }

        override fun newArray(size: Int): Array<CrewMember?> {
            return arrayOfNulls(size)
        }
    }
}

data class CastMember(val name: String, val character: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(character)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CastMember> {
        override fun createFromParcel(parcel: Parcel): CastMember {
            return CastMember(parcel)
        }

        override fun newArray(size: Int): Array<CastMember?> {
            return arrayOfNulls(size)
        }
    }

}

data class Genre(
    val id: Int = 0,
    val name: String = ""
)