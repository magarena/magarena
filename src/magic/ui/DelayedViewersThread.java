package magic.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

public final class DelayedViewersThread extends Thread {

	private static final DelayedViewersThread VIEWER_THREAD=new DelayedViewersThread();

	private final Map<DelayedViewer,Long> delayedViewers;
	
	private DelayedViewersThread() {
		delayedViewers=new HashMap<DelayedViewer,Long>();
        setDaemon(true);
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
			try { //wait
				if (delayedViewers.isEmpty()) {
					wait();
				}
				final long time = System.currentTimeMillis();
				for (Iterator<Map.Entry<DelayedViewer,Long>> iter = delayedViewers.entrySet().iterator(); iter.hasNext();) {
                    final Map.Entry<DelayedViewer, Long> entry = iter.next();
					if (entry.getValue() <= time) {
						entry.getKey().showDelayed();
                        iter.remove();
					}
				}

                //wait for 100ms
				wait(100);
			} catch (final InterruptedException ex) {
                throw new RuntimeException(ex);
			}
		}
	}

	public static DelayedViewersThread getInstance() {
		return VIEWER_THREAD;
	}
}
