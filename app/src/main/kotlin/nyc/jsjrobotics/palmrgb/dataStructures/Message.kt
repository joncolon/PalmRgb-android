package nyc.jsjrobotics.palmrgb.dataStructures

import nyc.jsjrobotics.palmrgb.database.MutableMessage


data class Message constructor(
        val message: String,
        val senderId: String,
        val messageId: String = UNKNOWN_ID,
        val timeStamp: Long = System.currentTimeMillis()
) {

    fun mutable(): MutableMessage {
        return MutableMessage(
                message,
                senderId,
                messageId,
                timeStamp
        )
    }

    companion object {
        private val UNKNOWN_ID = "unknown_id"
    }

}



