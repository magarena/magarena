package magic.ui;

public class GameControllerThread extends Thread {

	private final GameController controller;
	
	public GameControllerThread(final GameController controller) {
		this.controller=controller;
	}

	@Override
	public void run() {
		controller.runGame();
		System.err.println("Stopping game...");
	}
}
