package nyc.jsjrobotics.palmrgb.database

import nyc.jsjrobotics.palmrgb.dataStructures.Message


data class MutableMessage(
        var message: String,
        var senderId: String,
        var messageId: String,
        var timeStamp: Long
) {

    fun immutable(): Message {
        return Message(
                message,
                senderId,
                messageId,
                timeStamp
        )
    }
}



