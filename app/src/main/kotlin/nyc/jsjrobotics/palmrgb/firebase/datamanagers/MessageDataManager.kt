package nyc.jsjrobotics.palmrgb.firebase.datamanagers

import nyc.jsjrobotics.palmrgb.database.MutableMessage
import nyc.jsjrobotics.palmrgb.firebase.Firebase.MESSAGE_MODEL
import nyc.jsjrobotics.palmrgb.firebase.FirebaseDatabaseReference
import java.util.*
import javax.inject.Inject

class MessageDataManager
@Inject constructor() {

    fun uploadMessage(message: MutableMessage) {
        // creates a unique key identifier
        val uniqueMessageIdentifier = HashMap<String, Any>()
        // appends chatroom with unique key
        FirebaseDatabaseReference.messageReference.updateChildren(uniqueMessageIdentifier)
        // create a Firebase push key and set it as the message ID
        message.messageId = FirebaseDatabaseReference.messageReference.push().key
        // references the object using the message ID in the database
        val messageRoot = FirebaseDatabaseReference.messageReference.child(message.messageId)
        // assigns values to the children of the new message object
        val messageModelMap = HashMap<String, Any>()
        messageModelMap[MESSAGE_MODEL] = message
        // confirm changes
        messageRoot.updateChildren(messageModelMap)
    }
}
