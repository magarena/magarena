package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.CostPanel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class CardTable extends JTable {

	private static final long serialVersionUID = 113243L;
	
	private final CardViewer cardViewer;
	private final CardTableModel tableModel;
	
	public CardTable(List<MagicCardDefinition> defs, CardViewer cardViewer) {
		super(new CardTableModel(defs));
		
		this.tableModel = (CardTableModel) getModel();
		this.cardViewer = cardViewer;
		
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // allow only single row selection
		setRowHeight(20);
		
		// set column widths
		TableColumnModel model = getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			model.getColumn(i).setMinWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
		}
		
		// special renderer for mana symbols
		model.getColumn(1).setCellRenderer(new ManaCostCellRenderer());
		
		// listener to change card image on selection
		SelectionListener listener = new SelectionListener();
		getSelectionModel().addListSelectionListener(listener);
		getColumnModel().getSelectionModel().addListSelectionListener(listener);
		
		// listener to sort on column header click
		JTableHeader header = getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.addMouseListener(new ColumnListener());
		header.setReorderingAllowed(true);
	}
	
	public MagicCardDefinition getSelectedCard() {
		return tableModel.getCardDef(getSelectionModel().getLeadSelectionIndex());
	}
	
	public void setCards(List<MagicCardDefinition> defs) {
		tableModel.setCards(defs);
		tableChanged(new TableModelEvent(tableModel));
		repaint();
	}
	
	private class ColumnListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			TableColumnModel colModel = getColumnModel();
			int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
			int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

			if (modelIndex < 0) {
				return;
			}
			
			// sort
			tableModel.sort(modelIndex);
			
			// redraw
			tableChanged(new TableModelEvent(tableModel));
			repaint();
		}
	}
	
	private class ManaCostCellRenderer extends DefaultTableCellRenderer {
		private static final long serialVersionUID = 113245L;
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
			CostPanel myRender = new CostPanel((MagicManaCost) value);
			
			// match border and background formatting with default
			JLabel defaultRender = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
			myRender.setBackground(defaultRender.getBackground());
			myRender.setBorder(defaultRender.getBorder());
			myRender.setOpaque(true);
			
			return myRender;
		}
	}

	private class SelectionListener implements ListSelectionListener {
		public void valueChanged(ListSelectionEvent e) {
			// If cell selection is enabled, both row and column change events are fired
			if (e.getSource() == getSelectionModel() && getRowSelectionAllowed()) {
				// Row selection changed				
				MagicCardDefinition card = getSelectedCard();
				if (card != null) {
					cardViewer.setCard(card,0);
				}
			} /* else if (e.getSource() == getColumnModel().getSelectionModel() && getColumnSelectionAllowed() ){
				// Column selection changed
				int first = e.getFirstIndex();
				int last = e.getLastIndex();
			} */

			/* if (e.getValueIsAdjusting()) {
				// The mouse button has not yet been released
			} */
		}
	}
}
