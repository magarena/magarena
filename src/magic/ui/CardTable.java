package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.ui.viewer.CardViewer;
import magic.ui.widget.CostPanel;
import magic.ui.widget.TitleBar;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

public class CardTable extends JPanel {

	private static final long serialVersionUID = 113243L;
	
	private final CardViewer cardViewer;
	private final CardTableModel tableModel;
	private final JTable table;
	
	private MagicCardDefinition lastSelectedCard;
	
	public CardTable(final List<MagicCardDefinition> defs, final CardViewer cardViewer) {
		this(defs, cardViewer, "", false);
	}
	
	public CardTable(final List<MagicCardDefinition> defs, final CardViewer cardViewer, final String title, final boolean isDeck) {
		this.tableModel = new CardTableModel(defs, isDeck);
		this.table = new JTable(tableModel);
		this.cardViewer = cardViewer;
		this.lastSelectedCard = null;
		
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // otherwise horizontal scrollbar won't work
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // allow only single row selection
		table.setRowHeight(20);
		
		// set column widths
		TableColumnModel model = table.getColumnModel();
		for (int i = 0; i < model.getColumnCount(); i++) {
			model.getColumn(i).setMinWidth(CardTableModel.COLUMN_MIN_WIDTHS[i]);
		}
		
		// special renderer for mana symbols
		model.getColumn(CardTableModel.COST_COLUMN_INDEX).setCellRenderer(new ManaCostCellRenderer());
		
		// listener to change card image on selection
		SelectionListener listener = new SelectionListener();
		table.getSelectionModel().addListSelectionListener(listener);
		model.getSelectionModel().addListSelectionListener(listener);
		
		// listener to sort on column header click
		JTableHeader header = table.getTableHeader();
		header.setUpdateTableInRealTime(true);
		header.addMouseListener(new ColumnListener());
		header.setReorderingAllowed(true);
		
		// add table to scroll pane
		JScrollPane scrollpane = new JScrollPane(table);
		scrollpane.setBorder(null);
		scrollpane.setOpaque(false);
		scrollpane.getViewport().setOpaque(false);
		
		// add title
		setLayout(new BorderLayout());
		if (title.length() > 0) {
			TitleBar titleBar = new TitleBar(title);
			add(titleBar, BorderLayout.PAGE_START);
			add(scrollpane, BorderLayout.CENTER);
		} else {
			add(scrollpane);
		}			
	}
	
	public MagicCardDefinition getSelectedCard() {
		return tableModel.getCardDef(table.getSelectionModel().getLeadSelectionIndex());
	}
	
	void reselectLastCard() {
		// select previous card if possible
		if (lastSelectedCard != null) {
			int index = tableModel.findCardIndex(lastSelectedCard);
			if(index != -1) {
				table.getSelectionModel().setSelectionInterval(index,index);
			} else {
				// previous card no longer in list, so clear its record
				lastSelectedCard = null;
			}
		}		
	}
	
	public void setCards(List<MagicCardDefinition> defs) {
		tableModel.setCards(defs);
		table.tableChanged(new TableModelEvent(tableModel));
		table.repaint();
		reselectLastCard();
	}
	
	private class ColumnListener extends MouseAdapter {
		public void mouseClicked(MouseEvent e) {
			TableColumnModel colModel = table.getColumnModel();
			int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
			int modelIndex = colModel.getColumn(columnModelIndex).getModelIndex();

			if (modelIndex < 0) {
				return;
			}
			
			// sort
			tableModel.sort(modelIndex);
			
			// redraw
			table.tableChanged(new TableModelEvent(tableModel));
			table.repaint();
			
			reselectLastCard();
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
		public void valueChanged(final ListSelectionEvent e) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					// If cell selection is enabled, both row and column change events are fired
					if (e.getSource() == table.getSelectionModel() && table.getRowSelectionAllowed()) {
						// Row selection changed				
						MagicCardDefinition card = getSelectedCard();
						if (card != null) {
							lastSelectedCard = card;
							cardViewer.setCard(card,0);
						}
					} /* else if (e.getSource() == table.getColumnModel().getSelectionModel() && table.getColumnSelectionAllowed() ){
						// Column selection changed
						int first = e.getFirstIndex();
						int last = e.getLastIndex();
					} */

					/* if (e.getValueIsAdjusting()) {
						// The mouse button has not yet been released
					} */
				}
			});
		}
	}
}
