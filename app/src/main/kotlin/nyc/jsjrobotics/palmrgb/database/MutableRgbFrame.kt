package nyc.jsjrobotics.palmrgb.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = AppDatabase.RGB_FRAMES_TABLE_NAME)
data class MutableRgbFrame(
        @ColumnInfo(name = AppDatabase.FRAME_NAME_COLUMN)
        var frameName : String = "",
        @ColumnInfo(name = AppDatabase.COLOR_LIST_COLUMN)
        var colorList : List<Int> = emptyList()
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AppDatabase.FRAME_ID_COLUMN)
    var frameId : Long? = null

    companion object {
        val UNKNOWN_ID : Long = -1
        fun fromDatabaseString(value: String): MutableRgbFrame {
            return MutableRgbFrame()
        }

        fun toDatabaseString(value: MutableRgbFrame): String {
            return ""
        }
    }
}