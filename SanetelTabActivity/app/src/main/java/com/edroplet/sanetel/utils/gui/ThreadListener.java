package com.edroplet.sanetel.utils.gui;

public class ThreadListener implements Runnable {
	private Thread t;
	@Override
	public void run() {
		while(t.isAlive()) {
			
		}
	}
	public Thread getT() {
		return t;
	}
	public void setT(Thread t) {
		this.t = t;
	}
}
