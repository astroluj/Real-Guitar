package com.example.guitarplay;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ProximitySensorThread extends Thread {

	protected static boolean runFlag, startSensorFlag, stopSensorFlag;
	
	private SensorManager sensorM;
	private Handler handler;
	private Sensor sensor;
	// ���� ������ ��� ��
	private SensorEventListener sensorListener = new SensorEventListener() {
		public void onAccuracyChanged(Sensor sensor, int accuracy) {}
		public void onSensorChanged(SensorEvent event) {
			// ���� ������ �϶� ����
			if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {}
			if (event.sensor.getType () != Sensor.TYPE_PROXIMITY) return ;
			Message msg = new Message();
			msg.what = 0;
			msg.obj = (float) event.values[0];
			// Log.d ("D", "" +event.values[0]) ;
			// Log.d ("D", "" +sensor.getMaximumRange()) ;
			handler.sendMessage(msg);

		}
	};

	public ProximitySensorThread(Context context, Handler handler) {
		runFlag =false ;	// ������ run
		startSensorFlag =false ;	// ������ ����
		stopSensorFlag =false ;	// ������ ����
		this.handler =handler ;
		
		// ������ ���� �ý����� ��´�.
		sensorM =(SensorManager) context.getSystemService(Context.SENSOR_SERVICE) ;
		sensor =sensorM.getDefaultSensor(Sensor.TYPE_PROXIMITY) ;		// ���� ���� ���� ����
	}

	public void run() {
		Looper.prepare();
		while (!isInterrupted() && !runFlag) {
			try {
				sleep (1000) ;	// 1�� �������� ����
				if (startSensorFlag) {	// ���� ���� �޼����� ������
					// ������ ���� ��������� �ӵ��� �޴´�.
					sensorM.registerListener(sensorListener, sensor, SensorManager.SENSOR_DELAY_UI) ;
					startSensorFlag =false ;
				}
				else if (stopSensorFlag) {	// ���� ���� �޼����� ������
					// ���� Ž�� ����
					sensorM.unregisterListener(sensorListener) ;
					stopSensorFlag =false ;
				}
			} catch (InterruptedException e) {
				e.printStackTrace() ;
			}
		}
		//Looper.loop();
	}
}
