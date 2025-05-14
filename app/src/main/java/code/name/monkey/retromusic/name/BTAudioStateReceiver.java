public class BTAudioStateReceiver extends BroadcastReceiver {
    private AudioManager audioManager;
    private int originalVolume = -1;

    public void setAudioManager(AudioManager manager) {
        this.audioManager = manager;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null) return;

        if (BluetoothHeadset.ACTION_AUDIO_STATE_CHANGED.equals(intent.getAction())) {
            int state = intent.getIntExtra(BluetoothHeadset.EXTRA_STATE, BluetoothHeadset.STATE_DISCONNECTED);

            if (audioManager == null) return;

            if (state == BluetoothHeadset.STATE_CONNECTED || state == BluetoothHeadset.STATE_CONNECTING) {
                // 蓝牙正在播放音频，降低音量
                lowerVolume();
            } else if (state == BluetoothHeadset.STATE_DISCONNECTED) {
                // 蓝牙断开，恢复音量
                restoreVolume();
            }
        }
    }

    private void lowerVolume() {
        if (originalVolume == -1) {
            originalVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        }
        int newVolume = Math.max(originalVolume - 2, 0); // 音量降低2级
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
    }

    private void restoreVolume() {
        if (originalVolume != -1) {
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, originalVolume, 0);
            originalVolume = -1; // 重置
        }
    }
}
