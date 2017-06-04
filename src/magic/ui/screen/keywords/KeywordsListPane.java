package magic.ui.screen.keywords;

import java.awt.Color;
import java.io.IOException;
import javax.swing.JList;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
class KeywordsListPane extends JScrollPane {

    KeywordsListPane(final KeywordPanelB keywordPanel) throws IOException {

        JList<Keyword> list = new JList<>(KeywordsHelper.getKeywords());

        list.setLayoutOrientation(JList.VERTICAL_WRAP);
        list.setVisibleRowCount(-1);

        list.setOpaque(false);
        list.setCellRenderer(new KeywordsListCellRenderer());

        list.setForeground(Color.WHITE);
        list.setFont(list.getFont().deriveFont(14f));

        list.addListSelectionListener((e) -> {
            final Keyword keyword = list.getSelectedValue();
            keywordPanel.setKeyword(keyword);
        });

        list.setSelectedIndex(0);

        setViewportView(list);

        setOpaque(false);
        getViewport().setOpaque(false);
    }
}
