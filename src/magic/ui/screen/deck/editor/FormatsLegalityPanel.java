package magic.ui.screen.deck.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import magic.data.MagicFormat;
import magic.data.MagicPredefinedFormat;
import magic.data.MagicIcon;
import magic.model.MagicDeck;
import magic.ui.MagicImages;
import magic.translate.UiString;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class FormatsLegalityPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Formats";

    // fired when selection changes.
    public static final String CP_FORMAT_SELECTED = "c8d61cfc-568a-488d-a0fb-f37ef1a39192";

    private final MigLayout migLayout = new MigLayout();
    private final JScrollPane scrollpane = new JScrollPane();
    private final JList<DeckLegalityInfo> formatsJList = new JList<>();
    private boolean isAdjusting = false;
    private int lastSelectedRow = 0;
    private final ListSelectionListener selectionListener;
    private final JLabel titleLabel;

    public FormatsLegalityPanel() {

        formatsJList.setFocusable(true);
        formatsJList.setCellRenderer(new FormatsListCellRenderer());

        // add table to scroll pane
        scrollpane.setViewportView(formatsJList);
        scrollpane.getViewport().setBackground(Color.WHITE);
        scrollpane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.DARK_GRAY));

        titleLabel = new JLabel(UiString.get(_S1));
        titleLabel.setFont(getFont().deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Color.DARK_GRAY));

        setLayout(migLayout);
        refreshLayout();

        selectionListener = getListSelectionListener();

    }

    private ListSelectionListener getListSelectionListener() {
        return new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                isAdjusting = e.getValueIsAdjusting();
                if (!isAdjusting) {
                    lastSelectedRow = formatsJList.getSelectedIndex();
                    firePropertyChange(CP_FORMAT_SELECTED, false, true);
                }
            }
        };
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(titleLabel, "w 100%, h 21!, hidemode 3");
        add(scrollpane, "w 100%, h 100%");
    }

    void setDeck(MagicDeck aDeck) {
        formatsJList.removeListSelectionListener(selectionListener);
        formatsJList.setListData(getDeckFormatsLegality(aDeck));
        formatsJList.addListSelectionListener(selectionListener);
        formatsJList.setSelectedIndex(lastSelectedRow);
    }

    MagicFormat getSelectedFormat() {
        if (formatsJList.getSelectedIndex() == -1) {
            return formatsJList.getModel().getElementAt(0).getFormat();
        } else {
            return formatsJList.getModel().getElementAt(formatsJList.getSelectedIndex()).getFormat();
        }
    }

    private DeckLegalityInfo[] getDeckFormatsLegality(final MagicDeck aDeck) {
        final List<DeckLegalityInfo> lst = new ArrayList<>();
        for (MagicFormat aFormat : MagicPredefinedFormat.values()) {
            final DeckLegalityInfo deckLegality = new DeckLegalityInfo(aFormat);
            deckLegality.setIsLegal(aFormat.isDeckLegal(aDeck));
            lst.add(deckLegality);
        }
        return lst.toArray(new DeckLegalityInfo[0]);
    }

    private class FormatsListCellRenderer extends DefaultListCellRenderer {

        private final ImageIcon LEGAL_ICON = MagicImages.getIcon(MagicIcon.LEGAL);
        private final ImageIcon ILLEGAL_ICON = MagicImages.getIcon(MagicIcon.ILLEGAL);
        private final Color ALT_BCOLOR = new Color(242, 242, 242);

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

            final DeckLegalityInfo deckLegality = (DeckLegalityInfo) value;
            setText(" " + deckLegality.getFormat().getName());

            if (deckLegality.isLegal()) {
                setIcon(LEGAL_ICON);
                setFont(getFont().deriveFont(Font.BOLD));
                setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            } else {
                setIcon(ILLEGAL_ICON);
                setFont(getFont().deriveFont(Font.PLAIN));
                setForeground(isSelected ? Color.LIGHT_GRAY : Color.GRAY);
            }
            setBackground(isSelected
                    ? list.getSelectionBackground()
                    : index % 2 == 0
                            ? list.getBackground()
                            : ALT_BCOLOR
            );

            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(0, 6, 0, 4))
            );

            setPreferredSize(new Dimension(getWidth(), 23));

            return this;
        }

    }

    private class DeckLegalityInfo {

        private final MagicFormat magicFormat;
        private boolean isDeckLegal = true;

        DeckLegalityInfo(MagicFormat aFormat) {
            this.magicFormat = aFormat;
        }

        void setIsLegal(boolean b) {
            this.isDeckLegal = b;
        }

        boolean isLegal() {
            return isDeckLegal;
        }

        MagicFormat getFormat() {
            return magicFormat;
        }

    }

}
