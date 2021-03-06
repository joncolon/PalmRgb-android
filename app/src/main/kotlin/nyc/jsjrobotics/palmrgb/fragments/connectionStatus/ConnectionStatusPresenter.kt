package nyc.jsjrobotics.palmrgb.fragments.connectionStatus

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.OnLifecycleEvent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.content.LocalBroadcastManager
import io.reactivex.Observable
import nyc.jsjrobotics.palmrgb.androidInterfaces.DefaultPresenter
import nyc.jsjrobotics.palmrgb.runOnMainThread
import nyc.jsjrobotics.palmrgb.service.remoteInterface.HackdayLightsBackend
import nyc.jsjrobotics.palmrgb.service.remoteInterface.HackdayLightsInterface
import nyc.jsjrobotics.palmrgb.service.remoteInterface.RequestType
import javax.inject.Inject

class ConnectionStatusPresenter @Inject constructor() : DefaultPresenter() {
    lateinit var view: ConnectionStatusView
    private val updateConnectionStatus: BroadcastReceiver = buildUpdateReceiver()
    fun init(view: ConnectionStatusView, onHiddenChanged: Observable<Boolean>) {
        this.view = view
        onHiddenChanged.subscribe { hidden ->
            if (!hidden) {
                checkConnectionStatus()
            }
        }
    }

    private fun buildUpdateReceiver() : BroadcastReceiver{
        return object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (intent.hasExtra(HackdayLightsBackend.CONNECTION_CHECK_RESPONSE)) {
                    runOnMainThread {
                        view.displayConnected(intent.getBooleanExtra(HackdayLightsBackend.CONNECTION_CHECK_RESPONSE, false))
                    }
                }
            }

        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun registerUpdateConnectionReceiver() {
        activityNeeded.onNext{activity ->
            val intentFilter = IntentFilter(HackdayLightsBackend.CONNECTION_CHECK_RESPONSE)
            LocalBroadcastManager.getInstance(activity.applicationContext()).registerReceiver(updateConnectionStatus, intentFilter)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun checkConnectionOnResuem() {
        checkConnectionStatus()
    }
    private fun checkConnectionStatus() {
        HackdayLightsInterface.startRequest(RequestType.CHECK_CONNECTION)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun unregisterUpdateConnectionReceiver() {
        activityNeeded.onNext{activity ->
            LocalBroadcastManager.getInstance(activity.applicationContext()).unregisterReceiver(updateConnectionStatus)
        }
    }

}
