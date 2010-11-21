package magic.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

public class DelayedViewersThread extends Thread {

	private static final DelayedViewersThread VIEWER_THREAD=new DelayedViewersThread();

	private final Map<Component,Long> delayedViewers;
	
	private DelayedViewersThread() {

		delayedViewers=new HashMap<Component,Long>();
		start();
	}
	
	public synchronized void showViewer(final Component component,final int delay) {
		
		delayedViewers.put(component,System.currentTimeMillis()+delay);
		notify();
	}
	
	public synchronized void hideViewer(final Component component) {

		delayedViewers.remove(component);
		component.setVisible(false);
	}
	
	@Override
	public synchronized void run() {

		while (true) {
			
			try {
				if (delayedViewers.isEmpty()) {
					wait();
				}
				final long time=System.currentTimeMillis();
				for (final Component component : delayedViewers.keySet()) {
					
					final long delayedTime=delayedViewers.get(component);
					if (delayedTime<=time) {
						component.setVisible(true);
						delayedViewers.remove(component);
					}
				}
				wait(100);
			} catch (final InterruptedException ex) {
				System.out.println("Interrupted : "+ex.getMessage());
			}
		}
	}

	public static DelayedViewersThread getInstance() {
		
		return VIEWER_THREAD;
	}
}