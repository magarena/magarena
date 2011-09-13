package magic.ui;

import magic.model.MagicCardDefinition;
import magic.model.MagicDeckCardDefinition;
import magic.model.MagicManaCost;

import java.lang.Integer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CardTableModel implements TableModel {	
	/**
	 * List of event listeners. These listeners wait for something to happen
	 * with the table so that they can react. This is a must!
	 */
	private ArrayList<TableModelListener> listeners = new ArrayList<TableModelListener>();
	
	/**
	 * Adds a listener to the list that is notified each time a change to the
	 * model occurs.
	 * 
	 * @param l
	 *			the TableModelListener
	 */
	@Override
	public void addTableModelListener(TableModelListener l)
	{
		if (listeners.contains(l))
			return;
		listeners.add(l);
	}
	
	/**
	 * Removes a listener from the list that is notified each time a change to
	 * the model occurs.
	 * 
	 * @param l
	 *			the TableModelListener
	 */
	@Override
	public void removeTableModelListener(TableModelListener l)
	{
		listeners.remove(l);
	}
	
	public static final String[] COLUMN_NAMES = new String[] {"#", "Name", "CC", "Type", "Subtype", "Rarity", "Text"};
	public static final int[] COLUMN_MIN_WIDTHS = new int[] {40, 180, 140, 140, 140, 90, 2000};
	public static final int COST_COLUMN_INDEX = 2;
	
	private final boolean isDeck;
	
	private boolean[] isDesc = new boolean[] {false, false, false, false, false, false, false};
	private List<MagicDeckCardDefinition> cardDefinitions;
	private Comparator<MagicDeckCardDefinition> comp;
	
	public CardTableModel(final List<MagicCardDefinition> cardDefs, final boolean isDeck) {
		this.comp = MagicDeckCardDefinition.NAME_COMPARATOR_DESC;
		this.isDeck = isDeck;
		
		setCards(cardDefs);
	}
	
	public MagicCardDefinition getCardDef(int row) {
		if (row < 0 || row >= cardDefinitions.size()) {
			return null;
		}
		return cardDefinitions.get(row).getCard();
	}
	
	public void setCards(List<MagicCardDefinition> defs) {
		if (isDeck) {
			this.cardDefinitions = MagicDeckCardDefinition.condenseCopyCardList(defs);
		} else {
			this.cardDefinitions = MagicDeckCardDefinition.simpleCopyCardList(defs);
		}
		
		// re-sort if necessary
		if(comp != null) {
			Collections.sort(cardDefinitions, comp);
		}
	}
	
	public void sort(int column) {
		Comparator<MagicDeckCardDefinition> oldComp = comp;
		comp = null;
		
		switch(column) {
			case 0:		comp = (isDesc[column]) ? MagicDeckCardDefinition.NUM_COPIES_COMPARATOR_ASC : MagicDeckCardDefinition.NUM_COPIES_COMPARATOR_DESC;
						break;
			case 1:		comp = (isDesc[column]) ? MagicDeckCardDefinition.NAME_COMPARATOR_ASC : MagicDeckCardDefinition.NAME_COMPARATOR_DESC;
						break;
			case 2:		comp = (isDesc[column]) ? MagicDeckCardDefinition.CONVERTED_COMPARATOR_ASC : MagicDeckCardDefinition.CONVERTED_COMPARATOR_DESC;
						break;
			case 3:		comp = (isDesc[column]) ? MagicDeckCardDefinition.TYPE_COMPARATOR_ASC : MagicDeckCardDefinition.TYPE_COMPARATOR_DESC;
						break;
			case 5:		comp = (isDesc[column]) ? MagicDeckCardDefinition.RARITY_COMPARATOR_ASC : MagicDeckCardDefinition.RARITY_COMPARATOR_DESC;
						break;
		}
		
		if (comp != null) {
			// new sort
			Collections.sort(cardDefinitions, comp);
			isDesc[column] = !isDesc[column];			
		} else {
			// didn't select valid new sort -> reset to old
			comp = oldComp;
		}
	}
	
	/**
	 * Returns the most specific superclass for all the cell values in the
	 * column. This is used by the JTable to set up a default renderer and
	 * editor for the column.
	 * 
	 * @param columnIndex
	 *			the index of the column
	 * @return the common ancestor class of the object values in the model.
	 */
	@Override
	public Class<?> getColumnClass(int columnIndex)
	{
		switch (columnIndex) {
			case 1:		return MagicManaCost.class;
		}
		return String.class;
	}
	
	/**
	 * Returns the number of columns in the model. A JTable uses this method to
	 * determine how many columns it should create and display by default.
	 * 
	 * @return the number of columns in the model
	 * @see #getRowCount
	 */
	@Override
	public int getColumnCount()
	{
		return COLUMN_NAMES.length;
	}
	
	/**
	 * Returns the name of the column at columnIndex. This is used to initialize
	 * the table's column header name. Note: this name does not need to be
	 * unique; two columns in a table can have the same name.
	 * 
	 * @param columnIndex
	 *			the index of the column
	 * @return the name of the column
	 */
	@Override
	public String getColumnName(int columnIndex)
	{
		return COLUMN_NAMES[columnIndex];
	}
	
	/**
	 * Returns the number of rows in the model. A JTable uses this method to
	 * determine how many rows it should display. This method should be quick,
	 * as it is called frequently during rendering.
	 * 
	 * @return the number of rows in the model
	 * @see #getColumnCount
	 */
	@Override
	public int getRowCount()
	{
		return cardDefinitions.size();
	}
	
	/**
	 * Returns the value for the cell at columnIndex and rowIndex.
	 * 
	 * @param rowIndex
	 *			the row whose value is to be queried
	 * @param columnIndex
	 *			the column whose value is to be queried
	 * @return the value Object at the specified cell
	 */
	@Override
	public Object getValueAt(int rowIndex, int columnIndex)
	{
		MagicCardDefinition card = cardDefinitions.get(rowIndex).getCard();
		
		switch(columnIndex) {
			case 0:		if(isDeck) {
							return Integer.toString(cardDefinitions.get(rowIndex).getNumCopies());
						} else {
							return "";
						}
			case 1:		return card.getFullName();
			case 2:		return card.getCost();
			case 3:		return card.getLongTypeString();
			case 4:		return card.getSubTypeString();
			case 5:		return card.getRarityString();
		}
		
		return "";
	}
	
	/**
	 * Returns true if the cell at rowIndex and columnIndex is editable.
	 * Otherwise, setValueAt on the cell will not change the value of that cell.
	 * 
	 * @param rowIndex
	 *			the row whose value to be queried
	 * @param columnIndex
	 *			the column whose value to be queried
	 * @return true if the cell is editable
	 * @see #setValueAt
	 */
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return false; // don't allow editing
	}
	
	/**
	 * Sets the value in the cell at columnIndex and rowIndex to value.
	 * 
	 * @param value
	 *			the new value
	 * @param rowIndex
	 *			the row whose value is to be changed
	 * @param columnIndex
	 *			the column whose value is to be changed
	 * @see #getValueAt
	 * @see #isCellEditable
	 */
	@Override
	public void setValueAt(Object value, int rowIndex, int columnIndex)
	{
	}

}

