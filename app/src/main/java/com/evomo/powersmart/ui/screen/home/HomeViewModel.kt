package com.evomo.powersmart.ui.screen.home

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.evomo.powersmart.data.Resource
import com.evomo.powersmart.data.auth.AuthRepository
import com.evomo.powersmart.data.remote.model.EnergyData
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import info.mqtt.android.service.MqttAndroidClient
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val application: Application,
    private val authRepository: AuthRepository,
) : AndroidViewModel(application) {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _messageFlow = MutableSharedFlow<String>()
    val messageFlow: SharedFlow<String> = _messageFlow.asSharedFlow()

//    // StateFlow untuk active energy import
//    private val _activeEnergyImport = MutableStateFlow(49927072.0) // nilai awal
//    val activeEnergyImport: StateFlow<Double> = _activeEnergyImport.asStateFlow()
//
//    private val _addedEnergy = MutableStateFlow(0.0)
//    val addedEnergy: StateFlow<Double> = _addedEnergy.asStateFlow()
//
//    // StateFlow untuk indikator apakah status mesin baik, bahaya, peringatan, atau offline
//    private val _status = MutableStateFlow<Status?>(null)
//    val status: StateFlow<Status?> = _status.asStateFlow()
//
//    //
//    private val _energyIn = MutableStateFlow<EnergyIn?>(null)
//    val energyIn: StateFlow<EnergyIn?> = _energyIn.asStateFlow()
//
//    // StateFlow untuk waktu pembaruan terakhir
//    private val _lastUpdateTime = MutableStateFlow(getCurrentTime())
//    val lastUpdateTime: StateFlow<String> = _lastUpdateTime.asStateFlow()

    private var _previousEnergy = _state.value.activeEnergyImport

    private val _energyData = MutableStateFlow<EnergyData?>(null)
    val energyData: StateFlow<EnergyData?> = _energyData.asStateFlow()

    @SuppressLint("StaticFieldLeak")
    private val context: Context = application.applicationContext

    private lateinit var mqttAndroidClient: MqttAndroidClient
    private val gson = Gson() // Untuk parsing JSON

    init {
        setupMqtt()
//        simulateActiveEnergyImport()
        getProfilePictureUrl()
    }

    // Fungsi untuk mengatur MQTT
    private fun setupMqtt() {
        val brokerUrl = "tcp://34.44.202.231" // URL broker
        val clientId = MqttClient.generateClientId()
        mqttAndroidClient = MqttAndroidClient(context, brokerUrl, clientId)

        val options = MqttConnectOptions().apply {
            isCleanSession = true
        }

        try {
            mqttAndroidClient.connect(options, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    _messageFlow.tryEmit("MQTT Connected")
//                    Toast.makeText(context, "MQTT Connected", Toast.LENGTH_SHORT).show()
                    subscribeToTopic("evomo/final_data/loc_a")
                    updateStatus(Status.GOOD)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Timber.e("MQTT Connection Failed: ${exception?.message}")
//                    Toast.makeText(context, "MQTT Connection Failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                    _messageFlow.tryEmit("MQTT Connection Failed: ${exception?.message}")
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
//            Toast.makeText(context, "Failed to connect to MQTT: ${e.message}", Toast.LENGTH_SHORT).show()
            _messageFlow.tryEmit("Failed to connect to MQTT: ${e.message}")
        }

        mqttAndroidClient.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                Timber.e("MQTT Connection Lost: ${cause?.message}")
                updateStatus(Status.OFFLINE)
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                message?.payload?.let { payload ->
                    val jsonString = String(payload)
                    try {
                        val data = gson.fromJson(jsonString, EnergyData::class.java)
                        _energyData.value = data
                        println("MQTT Data Received: $data")
                        handleMqttData(data)
                    } catch (e: Exception) {
                        Timber.e("Failed to parse MQTT data: ${e.message}")
//                        Toast.makeText(
//                            context,
//                            "Failed to parse MQTT data: ${e.message}",
//                            Toast.LENGTH_SHORT
//                        ).show()
                        _messageFlow.tryEmit("Failed to parse MQTT data: ${e.message}")
                    }
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Timber.d("MQTT Delivery Complete")
            }
        })
    }

    private fun subscribeToTopic(topic: String) {
        mqttAndroidClient.subscribe(topic, 1, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                println("Subscribed to topic: $topic")
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                println("Failed to subscribe to topic: ${exception?.message}")
            }
        })
    }

    // Fungsi untuk menerima data dari MQTT
    private fun handleMqttData(data: EnergyData) {
        val newEnergy = data.active_energy_import

        // Update apakah nilai bertambah atau berkurang
        updateEnergyIn(
            when {
                newEnergy > _previousEnergy -> EnergyIn.INCREASE
                newEnergy < _previousEnergy -> EnergyIn.DECREASE
                else -> EnergyIn.NO_CHANGE
            }
        )
        _previousEnergy = newEnergy

        // Update status mesin
        val threshold = 50000000.0 // Nilai batas untuk menentukan status mesin
        updateStatus(
            when {
                newEnergy > threshold -> Status.DANGER
                newEnergy > threshold - 10000 -> Status.WARNING
                else -> Status.GOOD
            }
        )

        // Update nilai active_energy_import dan waktu pembaruan terakhir
        updateActiveEnergyImport(newEnergy)
        updateLastUpdateTime(data.reading_time)
    }

    // Fungsi untuk mensimulasikan data active_energy_import
    private fun simulateActiveEnergyImport() {

        val threshold = 50000000.0 // Nilai batas yang digunakan untuk menentukan status mesin

        viewModelScope.launch {
            while (true) {
                delay(1000L) // Interval pembaruan setiap 1 detik
                val energyToAdd = Random.nextDouble(-1000.0, 1000.0)
                updateAddedEnergy(energyToAdd)
                val newEnergy = _state.value.activeEnergyImport + energyToAdd

                // Update apakah nilai bertambah atau berkurang
                updateEnergyIn(
                    when {
                        newEnergy > _previousEnergy -> EnergyIn.INCREASE
                        newEnergy < _previousEnergy -> EnergyIn.DECREASE
                        else -> EnergyIn.NO_CHANGE
                    }
                )
                _previousEnergy = newEnergy

                // Update status mesin
                updateStatus(
                    when {
                        newEnergy > threshold -> Status.DANGER
                        newEnergy > threshold - 10000 -> Status.WARNING
                        else -> Status.GOOD
                    }
                )

                // Update nilai active_energy_import dan last update time
                updateActiveEnergyImport(newEnergy)
                updateLastUpdateTime(getCurrentTime())
            }
        }
    }

    private fun updateActiveEnergyImport(newValue: Double) {
        _state.update { _state.value.copy(activeEnergyImport = newValue) }
    }

    private fun updateAddedEnergy(newValue: Double) {
        _state.update { _state.value.copy(addedEnergy = newValue) }
    }

    private fun updateStatus(newValue: Status) {
        _state.update { _state.value.copy(status = newValue) }
    }

    private fun updateEnergyIn(newValue: EnergyIn) {
        _state.update { _state.value.copy(energyIn = newValue) }
    }

    private fun updateLastUpdateTime(newValue: String) {
        _state.update { _state.value.copy(lastUpdateTime = newValue) }
    }

    // Fungsi untuk mendapatkan waktu saat ini dalam format yang diinginkan
    private fun getCurrentTime(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yy - HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun getProfilePictureUrl() {
        val response = authRepository.getSignedInUser()

        if (response is Resource.Success) {
            _state.value = _state.value.copy(
                profilePictureUrl = response.data?.profilePictureUrl
            )
        }
    }
}