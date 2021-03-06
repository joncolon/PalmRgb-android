package nyc.jsjrobotics.palmrgb.service.remoteInterface

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface HackDayLightsApi {
    @GET(Paths.RAINBOW_PATH)
    fun triggerFunction(@Header("FUNCTION" ) function: String): Call<StatusResponse>

    @GET(Paths.RAINBOW_PATH)
    fun connectionCheck(): Call<String>

}

