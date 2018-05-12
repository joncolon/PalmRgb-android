package nyc.jsjrobotics.palmrgb.fragments.createMessage

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import nyc.jsjrobotics.palmrgb.Application
import nyc.jsjrobotics.palmrgb.DEBUG
import nyc.jsjrobotics.palmrgb.dataStructures.Message
import nyc.jsjrobotics.palmrgb.dataStructures.Palette
import nyc.jsjrobotics.palmrgb.firebase.datamanagers.MessageDataManager
import nyc.jsjrobotics.palmrgb.globalState.DeviceConstants
import nyc.jsjrobotics.palmrgb.globalState.SavedPaletteModel
import nyc.jsjrobotics.palmrgb.globalState.SharedPreferencesManager
import nyc.jsjrobotics.palmrgb.service.PalmRgbBackground
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateMessageModel @Inject constructor(private val application: Application,
                                             private val savedPalettesModel: SavedPaletteModel,
                                             private val sharedPreferencesManager: SharedPreferencesManager,
                                             private val messageDataManager: MessageDataManager) {
    var selectedPalette: Palette = savedPalettesModel.getStandardPalette()
    var displayedColors: MutableList<Int> = initialValues(); private set
    val displayChanged: PublishSubject<Boolean> = PublishSubject.create()
    val onDisplayChanged: Observable<Boolean> = displayChanged
    val currentUserId: String = setUserId()

    fun diodeRange(): IntRange = DeviceConstants.largeArrayRange

    fun uploadMessageToFirebase(messageTitle: String) {
        messageTitle.isNotEmpty().let {
            val message = Message(messageTitle, currentUserId)
            DEBUG("Sending message... $message")
            messageDataManager.uploadMessage(message.mutable())
        }
    }

    fun writeMessageToDatabase(messageTitle: String) {
        val data = ArrayList<Int>()
        data.addAll(displayedColors)
        val intent = PalmRgbBackground.saveMessage(messageTitle, data)
        application.startService(intent)
    }

    private fun initialValues(): MutableList<Int> = DeviceConstants.largeArrayRange.map { selectedPalette.colors.first() }.toMutableList()

    private fun setUserId(): String {
        var userId = sharedPreferencesManager.getCurrentUserId()
        if (userId == null) {
            userId = "User-${UUID.randomUUID().mostSignificantBits}"
            sharedPreferencesManager.setCurrentUserId(userId)
        }
        return userId
    }

    fun setDisplayingColors(displayingColors: List<Int>) {
        displayedColors.forEachIndexed { index, _ ->
            val nextColor: Int
            if (index < displayingColors.size) {
                nextColor = displayingColors[index]
            } else {
                nextColor = selectedPalette.colors[0]
            }
            displayedColors[index] = nextColor
        }
        displayChanged.onNext(true)
    }

    fun displayText(text: String) {
        val shownText = if (text.length <= 32) {
            text.substring(0, text.length)
        } else {
            text.substring(text.lastIndex - 32, text.length)
        }
        setDisplayingColors(generateColors(shownText))
    }

    private fun generateColors(shownText: String): List<Int> {
        return shownText.map { it.toInt() }
                .map { selectedPalette.colors[it % selectedPalette.colors.size] }
    }
}
