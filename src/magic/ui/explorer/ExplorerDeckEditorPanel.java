package magic.ui.explorer;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class ExplorerDeckEditorPanel extends JPanel {
    public abstract boolean isDeckEditor();
    public abstract void updateCardPool();
}
