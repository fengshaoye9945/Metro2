// 文件路径: app/src/main/java/code/name/monkey/retromusic/BTAudioStateReceiver.kt

package code.name.monkey.retromusic

import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

class BTAudioStateReceiver : BroadcastReceiver() {

    private var audioManager: AudioManager? = null
    private var originalVolume = -1

    fun setAudioManager(manager: AudioManager) {
        audioManager = manager
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action != BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED) return

        val state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_DISCONNECTED)
        val am = audioManager ?: return

        when (state) {
            BluetoothHeadset.STATE_CONNECTED,
            BluetoothHeadset.STATE_CONNECTING -> lowerVolume(am)
            BluetoothHeadset.STATE_DISCONNECTED -> restoreVolume(am)
        }
    }

    private fun lowerVolume(am: AudioManager) {
        if (originalVolume == -1) {
            originalVolume = am.getStreamVolume(AudioManager.STREAM_MUSIC)
        }
        val newVolume = Math.max(originalVolume - 2, 0)
        am.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0)
    }

    private fun restoreVolume(am: AudioManager) {
        if (originalVolume != -1) {
            am.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0)
            originalVolume = -1
        }
    }
}
