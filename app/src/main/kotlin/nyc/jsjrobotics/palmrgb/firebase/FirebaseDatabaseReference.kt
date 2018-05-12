package nyc.jsjrobotics.palmrgb.firebase

import com.google.firebase.database.FirebaseDatabase
import nyc.jsjrobotics.palmrgb.firebase.Firebase.MESSAGES


object FirebaseDatabaseReference {

    val database = FirebaseDatabase.getInstance()
    val messageReference = database.getReference(MESSAGES)

}
