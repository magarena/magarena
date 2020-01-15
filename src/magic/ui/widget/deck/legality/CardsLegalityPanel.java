package magic.ui.widget.deck.legality;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import magic.data.MagicFormat;
import magic.data.MagicIcon;
import magic.model.MagicCardDefinition;
import magic.model.MagicDeck;
import magic.translate.MText;
import magic.translate.StringContext;
import magic.ui.FontsAndBorders;
import magic.ui.MagicImages;
import magic.ui.mwidgets.MScrollPane;
import magic.ui.screen.decks.ICardsTableListener;
import net.miginfocom.swing.MigLayout;

@SuppressWarnings("serial")
public class CardsLegalityPanel extends JPanel {

    // translatable strings
    private static final String _S1 = "Deck";
    @StringContext(eg="Memnite <b>is illegal/banned/restricted</b> in Standard.")
    private static final String _S2 ="%s <b>%s</b> in %s.";
    @StringContext(eg="Illegal deck size for Vintage.")
    private static final String _S3 = "Illegal deck size for %s.";
    private static final String _S4 = "A minimum of %d cards is required.";

    // fired when selection changes.
    public static final String CP_CARD_SELECTED = "019f0246-bd63-4efd-a7cf-fefabea053e3";

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private static final int ROW_HEIGHT = 23; //pixels

    private final MigLayout migLayout = new MigLayout();
    private final MScrollPane scrollpane = new MScrollPane();
    private final CardsLegalityTableModel tableModel;
    private final JTable table;
    private boolean isAdjusting = false;
    private int lastSelectedRow = -1;
    private final JLabel titleLabel;
    private  MagicFormat magicFormat;
    private final List<ICardsTableListener> listeners = new ArrayList<>();

    public CardsLegalityPanel() {

        this.tableModel = new CardsLegalityTableModel();

        this.table = new JTable(tableModel) {
            private final Color defaultForeColor = getForeground();
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                final CardLegalityInfo cardLegality = tableModel.getCardLegality(row);
                final boolean isRowSelected = table.isRowSelected(row);
                if (isRowSelected) {
                    c.setForeground(cardLegality.isCardLegal() ? table.getSelectionForeground() : Color.LIGHT_GRAY);
                } else {
                    if (cardLegality.isCardLegal()) {
                        c.setForeground(defaultForeColor);
                    } else {
                        c.setForeground(Color.GRAY);
                    }
                }
                return c;
            }
        };

        table.setDefaultRenderer(Object.class, new HideCellFocusRenderer());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(ROW_HEIGHT);
        table.setGridColor(GRID_COLOR);
        table.setTableHeader(null);
        table.getSelectionModel().addListSelectionListener(getTableListSelectionListener());
        table.addMouseListener(getTableMouseAdapter());
        table.setShowVerticalLines(false);

        final TableColumnModel model = table.getColumnModel();
        setColumnWidths(model);
        // special renderer for legality indicator icon.
        model.getColumn(0).setCellRenderer(new LegalityCellRenderer());

        // add table to scroll pane
        scrollpane.setViewportView(table);
        scrollpane.setBorder(FontsAndBorders.NO_BORDER);
        scrollpane.setBackground(Color.WHITE);

        titleLabel = new JLabel(MText.get(_S1));
        titleLabel.setFont(getFont().deriveFont(Font.BOLD));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.DARK_GRAY));

        setLayout(migLayout);
        refreshLayout();

        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

    }

    private ListSelectionListener getTableListSelectionListener() {
        return e -> {
            isAdjusting = e.getValueIsAdjusting();
            if (!isAdjusting) {
                lastSelectedRow = table.getSelectedRow();
                notifyCardSelected(tableModel.getCardDef(lastSelectedRow));
                firePropertyChange(CP_CARD_SELECTED, false, true);
            }
        };
    }

    private void notifyOnLeftClick(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> listener.onLeftClick(card));
        }
    }

    private void notifyOnRightClick(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> listener.onRightClick(card));
        }
    }

    private MouseAdapter getTableMouseAdapter() {
        return new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isAdjusting) {
                    int mouseRow = table.rowAtPoint(e.getPoint());
                    MagicCardDefinition card = tableModel.getCardDef(mouseRow);
                    card = card == null ? MagicCardDefinition.UNKNOWN : card;
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        notifyOnLeftClick(card);
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        notifyOnRightClick(card);
                    }
                }
            }
        };
    }

    private void refreshLayout() {
        removeAll();
        migLayout.setLayoutConstraints("flowy, insets 0, gap 0");
        add(titleLabel, "w 100%, h 21!, hidemode 3");
        add(scrollpane.component(), "w 100%, h 100%");
    }

    private void setColumnWidths(final TableColumnModel model) {
        for (int i = 0; i < model.getColumnCount(); i++) {
            model.getColumn(i).setMinWidth(CardsLegalityTableModel.COLUMN_MIN_WIDTHS[i]);
            model.getColumn(i).setPreferredWidth(CardsLegalityTableModel.COLUMN_MIN_WIDTHS[i]);
        }
        model.getColumn(0).setMaxWidth(CardsLegalityTableModel.COLUMN_MIN_WIDTHS[0]);
    }

    public MagicCardDefinition getSelectedCard() {
        if (table.getSelectedRow() >= 0) {
            return tableModel.getCardDef(table.getSelectedRow());
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }

    public void setDeck(final MagicDeck aDeck, final MagicFormat aFormat) {
        this.magicFormat = aFormat;
        final boolean isValidDeckSize = aDeck.size() >= aFormat.getMinimumDeckSize();
        scrollpane.setVisible(isValidDeckSize);
        if (isValidDeckSize) {
            tableModel.showDeckLegality(aDeck, aFormat);
            table.revalidate();
            table.repaint();
            setSelectedRow();
        } else {
            repaint();
        }
    }

    private void setSelectedRow() {
        if (lastSelectedRow >= 0 && lastSelectedRow < table.getRowCount()) {
            table.setRowSelectionInterval(lastSelectedRow, lastSelectedRow);
        } else {
            table.setRowSelectionInterval(0, 0);
        }
    }

    private static class HideCellFocusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBorder(noFocusBorder);
            return this;
        }
    }

    private static class LegalityCellRenderer extends DefaultTableCellRenderer {

        private static final JLabel BANNED_ICON = new JLabel(MagicImages.getIcon(MagicIcon.BANNED));
        private static final JLabel RESTRICTED_ICON = new JLabel(MagicImages.getIcon(MagicIcon.RESTRICTED));
        private static final JLabel LEGAL_ICON = new JLabel(MagicImages.getIcon(MagicIcon.LEGAL));
        private static final JLabel ILLEGAL_ICON = new JLabel(MagicImages.getIcon(MagicIcon.ILLEGAL));

        private JLabel getLegalityIcon(final CardLegalityInfo dfl) {
            switch (dfl.getLegality()) {
                case Banned: return BANNED_ICON;
                case Illegal: return ILLEGAL_ICON;
                case Legal: return LEGAL_ICON;
                case Restricted: return RESTRICTED_ICON;
                case TooManyCopies: return RESTRICTED_ICON;
            }
            throw new RuntimeException("Legality icon not found.");
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final CardsLegalityTableModel tableModel = (CardsLegalityTableModel) table.getModel();
            final CardLegalityInfo cardLegality = tableModel.getCardLegality(row);

            final JLabel lbl = getLegalityIcon(cardLegality);

            lbl.setToolTipText(String.format("<html>%s</html>",
                    MText.get(_S2,
                            cardLegality.getCardName(),
                            cardLegality.getLegality().getDescription(),
                            cardLegality.getFormat().getName()))
            );

            // match border and background formatting with default
            final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
            lbl.setOpaque(defaultRender.isOpaque());
            lbl.setBorder(defaultRender.getBorder());

            if (isSelected) {
                lbl.setForeground(table.getSelectionForeground());
                lbl.setBackground(table.getSelectionBackground());
            } else {
                lbl.setForeground(getForeground());
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure
                // why.
                lbl.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));

            }
            lbl.setBorder(noFocusBorder);
            return lbl;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (magicFormat != null) {
            final Graphics2D g2d = (Graphics2D)g.create();
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.setFont(FontsAndBorders.FONT2);
            g2d.drawString(MText.get(_S3, magicFormat.getName()), 20, 60);
            g2d.drawString(MText.get(_S4, magicFormat.getMinimumDeckSize()), 20, 84);
            g2d.dispose();
        }
    }

    private void notifyCardSelected(MagicCardDefinition card) {
        for (ICardsTableListener listener : listeners) {
            SwingUtilities.invokeLater(() -> {
                listener.onCardSelected(card == null ? MagicCardDefinition.UNKNOWN : card);
            });
        }
    }

    void setCardsTableListeners(ICardsTableListener[] listeners) {
        this.listeners.clear();
        this.listeners.addAll(Arrays.asList(listeners));
    }
}
