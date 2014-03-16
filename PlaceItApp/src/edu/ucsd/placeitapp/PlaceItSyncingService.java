package edu.ucsd.placeitapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.os.IBinder;
import android.util.Log;

public class PlaceItSyncingService extends Service {
	
	private static final long INTERVAL = 20000; // 15000ms
	private long prevTime; 
	
	Context serviceContext; 
	Thread syncThread; 
	boolean running; 

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		running = false; 
		Log.d("Debug", "Service disabled.");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		serviceContext = this; 
		prevTime = 0; //to sync immediately on start
		running = true; 
		SyncLoop loop = new SyncLoop();
		syncThread = new Thread(loop);
		syncThread.start();
	}

	
	private class SyncLoop implements Runnable {

		@Override
		public void run() {
			while (running) {
				if ((System.currentTimeMillis() - prevTime) > INTERVAL) {
					prevTime = System.currentTimeMillis(); 
					SyncClient.sync(serviceContext);
					Log.d("Syncing", "Sync Action activated."); 
				}
			}
		}
	}

}
