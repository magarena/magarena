package magic.ui;

import java.util.HashMap;
import java.util.Map;

public class DelayedViewersThread extends Thread {

	private static final DelayedViewersThread VIEWER_THREAD=new DelayedViewersThread();

	private final Map<DelayedViewer,Long> delayedViewers;
	
	private DelayedViewersThread() {

		delayedViewers=new HashMap<DelayedViewer,Long>();
		start();
	}
	
	public synchronized void showViewer(final DelayedViewer delayedViewer,final int delay) {
		
		delayedViewers.put(delayedViewer,System.currentTimeMillis()+delay);
		notify();
	}
	
	public synchronized void hideViewer(final DelayedViewer delayedViewer) {

		delayedViewers.remove(delayedViewer);
		delayedViewer.hideDelayed();
	}
	
	@Override
	public synchronized void run() {

		while (true) {
			
			try {
				if (delayedViewers.isEmpty()) {
					wait();
				}
				final long time=System.currentTimeMillis();
				for (final DelayedViewer delayedViewer : delayedViewers.keySet()) {
					
					final long delayedTime=delayedViewers.get(delayedViewer);
					if (delayedTime<=time) {
						delayedViewer.showDelayed();
						delayedViewers.remove(delayedViewer);
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