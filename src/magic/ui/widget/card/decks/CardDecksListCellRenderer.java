package magic.ui.widget.card.decks;

import java.awt.Component;
import java.io.File;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import org.apache.commons.io.FilenameUtils;

@SuppressWarnings("serial")
class CardDecksListCellRenderer  extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final File deckFile = (File) value;
        final String filename = FilenameUtils.removeExtension(deckFile.getName());
        final Component c = super.getListCellRendererComponent(list, filename, index, isSelected, cellHasFocus);
        return c;
    }

}
