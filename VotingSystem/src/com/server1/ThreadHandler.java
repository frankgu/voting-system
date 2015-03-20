package com.server1;

import java.io.IOException;
import java.net.DatagramPacket;

public class ThreadHandler implements Runnable {

	private DatagramPacket packet = null;

	public ThreadHandler(DatagramPacket packet) {

		this.packet = packet;

	}
	
	@Override
	public void run() {	
		// ------get the checksum value for the data.
		try {
			Connection.getInstance().processPacket(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
