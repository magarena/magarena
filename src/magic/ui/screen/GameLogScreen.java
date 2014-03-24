package magic.ui.screen;

import magic.MagicMain;
import java.nio.file.Paths;

@SuppressWarnings("serial")
public class GameLogScreen extends TextFileReaderScreen {

    public GameLogScreen() {
        super(Paths.get(MagicMain.getLogsPath()).resolve("game.log"));
    }

}
