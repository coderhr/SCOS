package es.source.code.br;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import es.source.code.service.UpdateService;

public class DeviceStartedListener extends BroadcastReceiver {

    private static final String TAG = "BootBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.i(TAG, "BootBroadcastReceiver onReceive(), Do thing!");
            Intent mIntent = new Intent(context, UpdateService.class);
            mIntent.putExtra("string", "Success");
            context.startService(mIntent);
        }
    }
}
