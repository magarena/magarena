package magic.ui.widget.cards.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.helpers.ColorHelper;
import magic.ui.utility.MagicStyle;
import magic.ui.widget.CostPanel;

@SuppressWarnings("serial")
public class CardsJTable extends JTable
    implements MouseListener, MouseMotionListener {

    private static final int ROW_HEIGHT = 20; // pixels

    private static final Color GRID_COLOR = new Color(194, 197, 203);
    private Color DEFAULT_GRID_COLOR;

    private final Border NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    private static final Border SELECTED_BORDER =
            BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 1, 0, MagicStyle.getRolloverColor()),
                BorderFactory.createEmptyBorder(0, 1, 0, 0)
            );

    private final Color defaultForeColor = getForeground();
    private int mouseOverRow = -1;
    private final CardTableModel tableModel;


    public CardsJTable(CardTableModel dm) {
        super(dm);
        this.tableModel = dm;
        setDefaultProperties();
        setColumnRenderers();
        setDefaultColumnProperties();
    }

    private void setMouseListeners() {
        if (CardsTableStyle.getStyle() == CardsTableStyle.THEME) {
            addMouseMotionListener(this);
            addMouseListener(this);
        } else {
            removeMouseMotionListener(this);
            removeMouseListener(this);
        }
    }

    private void setDefaultProperties() {
        DEFAULT_GRID_COLOR = getGridColor();
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
        setRowHeight(ROW_HEIGHT);
        setOpaque(false);
        setForeground(ColorHelper.getOppositeColor(getForeground()));
        setStyleProperties();
    }

    private void setStyleProperties() {
        setShowGrid(CardsTableStyle.getStyle() != CardsTableStyle.THEME);
        setGridColor(CardsTableStyle.getStyle() == CardsTableStyle.LIGHT ? GRID_COLOR : DEFAULT_GRID_COLOR);
        setMouseListeners();
    }

    private void setDefaultColumnProperties() {
        final TableColumnModel cm = getColumnModel();
        // set initial column widths.
        for (int i = 0; i < cm.getColumnCount(); i++) {
            cm.getColumn(i).setMinWidth(CardTableColumn.getMinWidth(i));
            cm.getColumn(i).setPreferredWidth(CardTableColumn.getMinWidth(i));
        }
        final JTableHeader header = getTableHeader();
        header.setReorderingAllowed(true);
        final DefaultTableCellRenderer renderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        // center the column header captions.
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /*
    Default cell renderer
    */
    private static final DefaultTableCellRenderer defaultCellRenderer = new DefaultTableCellRenderer() {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            final JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            c.setBorder(noFocusBorder);
            return c;
        }
    };

    private static final DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    static {
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void setCostColumnRenderer() {
        getColumnModel().getColumn(CardTableColumn.Cost.ordinal())
                .setCellRenderer(getCostRenderer());
    }

    private void setColCentered(CardTableColumn col) {
        getColumnModel().getColumn(col.ordinal()).setCellRenderer(centerRenderer);
    }

    private void setColumnRenderers() {
        setColCentered(CardTableColumn.Rating);
        setColCentered(CardTableColumn.Power);
        setColCentered(CardTableColumn.Toughness);
        setCostColumnRenderer();
        setDefaultRenderer(Object.class, defaultCellRenderer);
    }

    /*
    Per-cell rendering.
    */
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        switch (CardsTableStyle.getStyle()) {
            case LIGHT: return prepareDefaultRenderer(renderer, row, column);
            case THEME: return prepareThemedRenderer(renderer, row, column);
            case DARK: return prepareMidnightRenderer(renderer, row, column);
            default: throw new UnsupportedOperationException();
        }
    }

    private DefaultTableCellRenderer getCostRenderer() {
        switch (CardsTableStyle.getStyle()) {
            case LIGHT: return defaultCostRenderer;
            case THEME: return themedCostRenderer;
            case DARK: return midnightCostRenderer;
            default: throw new UnsupportedOperationException();
        }
    }

    void doSwitchStyle() {
        CardsTableStyle.setNextStyle();
        setStyle(CardsTableStyle.getStyle());
    }

    /******************************************************************
     * DARK THEME
     ******************************************************************/
    private static final Color DEF_ROW_COLOR =
            UIManager.getLookAndFeelDefaults().getColor("Table:\"Table.cellRenderer\".background");

    private static final Color DEF_ROW_COLOR_NEG =
            ColorHelper.getOppositeColor(DEF_ROW_COLOR);

    private static final Color ALT_ROW_COLOR =
            UIManager.getLookAndFeelDefaults().getColor("Table.alternateRowColor");

    private static final Color ALT_ROW_COLOR_NEG =
            ColorHelper.getOppositeColor(ALT_ROW_COLOR);

    //
    // default cell renderer
    //
    private Component prepareMidnightRenderer(TableCellRenderer renderer, int row, int column) {

        Component cell = super.prepareRenderer(renderer, row, column);

        final MagicCardDefinition card = tableModel.getCardDef(row);
        final boolean isRowSelected = isRowSelected(row);
        final boolean isAlternate = row % 2 == 0;

        ((JComponent) cell).setOpaque(true);
        ((JComponent) cell).setBorder(NO_FOCUS_BORDER);

        // Text Color
        final Color c1 = isRowSelected
                ? card.isInvalid()
                        ? Color.LIGHT_GRAY
                        : getSelectionForeground()
                : card.isInvalid()
                        ? Color.GRAY
                        : defaultForeColor;
        final Color c2 = ColorHelper.getOppositeColor(c1);
        cell.setForeground(c2);
        cell.setBackground(isRowSelected
                ? getSelectionBackground()
                : isAlternate ? DEF_ROW_COLOR_NEG : ALT_ROW_COLOR_NEG
        );

        return cell;
    }

    //
    // Mana cost renderer
    //
    private final DefaultTableCellRenderer midnightCostRenderer = new DefaultTableCellRenderer() {

        private final Border CostBorder = new EmptyBorder(1, 1, 1, 4);

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            // match border and background formatting with default
            final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            final MagicCardDefinition card = tableModel.getCardDef(row);
            final CostPanel costPanel = new CostPanel(card.hasCost() ? card.getCost() : null);
            final boolean isRowSelected = isRowSelected(row);
            final boolean isAlternate = row % 2 == 0;

            costPanel.setBackground(isSelected
                ? getSelectionBackground()
                : isAlternate
                        ? DEF_ROW_COLOR_NEG
                        : ALT_ROW_COLOR_NEG
            );
            costPanel.setForeground(defaultRender.getForeground());
            costPanel.setOpaque(defaultRender.isOpaque());
            costPanel.setBorder(CostBorder);
            return costPanel;
        }
    };


    /******************************************************************
     * DEFAULT THEME
     ******************************************************************/
    private static final Color SELECT_BCOLOR =
            UIManager.getColor("Table[Enabled+Selected].textBackground");

    private static final Color ALTERNATE_COLOR =
            UIManager.getColor("Table.alternateRowColor");

    //
    // default cell renderer
    //
    private Component prepareDefaultRenderer(TableCellRenderer renderer, int row, int column) {

        final Component cell = super.prepareRenderer(renderer, row, column);
        final MagicCardDefinition card = tableModel.getCardDef(row);
        final boolean isRowSelected = isRowSelected(row);
        final boolean isAlternate = row % 2 == 0;

        ((JComponent) cell).setOpaque(true);
        ((JComponent) cell).setBorder(NO_FOCUS_BORDER);

        /*
        Set text color.
        */
        cell.setBackground(isRowSelected
                ? SELECT_BCOLOR
                : isAlternate
                        ? ALTERNATE_COLOR
                        // have to create a new color object because Nimbus returns
                        // a color of type DerivedColor, which behaves strange, not sure why.
                        : new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue())
        );
        cell.setForeground(isRowSelected
                ? card.isInvalid()
                        ? Color.LIGHT_GRAY
                        : getSelectionForeground()
                : card.isInvalid()
                        ? Color.GRAY
                        : defaultForeColor
        );

        return cell;
    }

    //
    // Mana cost renderer
    //
    private static final DefaultTableCellRenderer defaultCostRenderer = new DefaultTableCellRenderer() {

        private MagicManaCost getManaCost(MagicCardDefinition card, Object value) {
            return card.hasCost() ? (MagicManaCost)value : null;
        }

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final MagicCardDefinition card = ((CardTableModel)table.getModel()).getCardDef(row);
            final CostPanel panel = new CostPanel(getManaCost(card, value));

            // match border and background formatting with default
            final JComponent defaultRender = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);

            panel.setOpaque(false);
            panel.setBorder(defaultRender.getBorder());

            if (isSelected) {
                panel.setForeground(table.getSelectionForeground());
                panel.setBackground(Color.RED);
            } else {
                panel.setForeground(getForeground());
                // We have to create a new color object because Nimbus returns
                // a color of type DerivedColor, which behaves strange, not sure
                // why.
//                panel.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue()));

            }
            panel.setBorder(noFocusBorder);
            return panel;
        }
    };


    /******************************************************************
     * TRANSLUCENT THEME
     ******************************************************************/

    //
    // default cell renderer
    //
    private Component prepareThemedRenderer(TableCellRenderer renderer, int row, int column) {

        final Component cell = super.prepareRenderer(renderer, row, column);
        final MagicCardDefinition card = tableModel.getCardDef(row);
        final boolean isRowSelected = isRowSelected(row);

        /*
        Set text color.
        */
        cell.setForeground(card.isInvalid()
                ? Color.GRAY
                : ColorHelper.getOppositeColor(defaultForeColor)
        );

        /*
        default cell properties
        */
        final int ordinal = convertColumnIndexToModel(column);
        if (CardTableColumn.values()[ordinal] != CardTableColumn.Cost) {
            if (cell instanceof JComponent) {
                final JComponent jc = (JComponent) cell;
                jc.setBorder(isRowSelected ? SELECTED_BORDER : NO_FOCUS_BORDER);
                jc.setOpaque(false);
            }
        }


        /*
        Highlight row on mouse over.
        */
        if (row == mouseOverRow) {
            cell.setForeground(MagicStyle.getRolloverColor());
        }

        return cell;
    }

    //
    // Mana cost renderer
    //
    private static final DefaultTableCellRenderer themedCostRenderer = new DefaultTableCellRenderer() {

        private final Border DEFAULT_BORDER = new EmptyBorder(1, 1, 1, 1);

        private final Border SELECTED_COST_BORDER =
                BorderFactory.createCompoundBorder(
                        SELECTED_BORDER,
                        BorderFactory.createEmptyBorder(0, 0, 0, 1)
                );

        @Override
        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int col) {

            final MagicCardDefinition card =
                    ((CardTableModel)table.getModel()).getCardDef(row);

            final CostPanel panel = new CostPanel(card.hasCost()
                    ? (MagicManaCost) value
                    : null
            );

            panel.setOpaque(false);
            panel.setBorder(isSelected ? SELECTED_COST_BORDER : DEFAULT_BORDER);

            return panel;
        }
    };

    @Override
    public void mouseClicked(MouseEvent e) {
        // NA
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // NA
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // NA
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // NA
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOverRow = -1;
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // NA
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        final int row = rowAtPoint(e.getPoint());
        if (row != mouseOverRow) {
            mouseOverRow = row;
            repaint();
        }
    }

    void setStyle(CardsTableStyle newStyle) {
        CardsTableStyle.setStyle(newStyle);
        setColumnRenderers();
        setStyleProperties();
        repaint();
    }
}
