package support.ui;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.ComboBoxModel;
import support.ui.SelectionListener.SelectionAction;

/**
 * A generic data storage used by {@link GenericJComboBox}. By having this class be generic, it allows for accessing the
 * data with type safety and no need to cast.
 *
 * @author jak2
 */
public class GenericComboBoxModel<T> extends GenericImmutableListModel<T> implements ComboBoxModel
{
    private T _selectedItem;
    private boolean _isSelectionInProgress;

    public GenericComboBoxModel(Iterable<T> data)
    {
        super(data);

        _selectedItem = this.hasElements() ? this.getElementAt(0) : null;
        _isSelectionInProgress = false;
    }
    
    public GenericComboBoxModel(Iterable<T> data, T initialSelection)
    {
        super(data);
        
        _selectedItem = findMatchingObject(initialSelection) != null ? initialSelection : null;
        _isSelectionInProgress = false;
    }

    public GenericComboBoxModel()
    {
        this(Collections.<T>emptyList());
    }

    @Override
    public void setSelectedItem(Object item)
    {
        //If item is not the same as _selectedItem
        if((_selectedItem != null && !_selectedItem.equals(item)) || (_selectedItem == null && item != null))
        {
            if(item == null)
            {
                this.setSelectedItemInternal(null);
            }
            else
            {
                //If item is in the model, then it will be returned in a typesafe manner, if it is not then null will be
                //returned and no selection should be made
                T itemInModel = findMatchingObject(item);
                if(itemInModel != null)
                {
                    this.setSelectedItemInternal(itemInModel);
                }
            }
        }
    }

    public void setGenericSelectedItem(T item)
    {
        //If item is not the same as _selectedItem
        if((_selectedItem != null && !_selectedItem.equals(item)) || (_selectedItem == null && item != null))
        {
            if(item == null)
            {
                this.setSelectedItemInternal(null);
            }
            //If the selection is contained in the data
            else if(this.getElements().contains(item))
            {
                this.setSelectedItemInternal(item);
            }
        }
    }
    
    private void setSelectedItemInternal(T item)
    {
        boolean cancelled = notifySelectionListeners(_selectedItem, item, true);
        if(!cancelled)
        {
            _selectedItem = item;

            //Matches behavior of javax.swing.DefaultComboBoxModel
            fireContentsChanged(this, -1, -1);
        }
    }
    
    private final List<SelectionListener<T>> _listeners = new CopyOnWriteArrayList<SelectionListener<T>>();
    
    void addSelectionListener(SelectionListener<T> listener)    
    {
        _listeners.add(listener);
    }
    
    void removeSelectionListener(SelectionListener<T> listener)
    {
        _listeners.remove(listener);
    }
    
    void transferSelectionListeners(GenericComboBoxModel<T> otherModel)
    {
        _listeners.addAll(otherModel._listeners);
        otherModel._listeners.clear();
    }
    
    boolean notifySelectionListeners(T currValue, T newValue, boolean isCancellable)
    {
        _isSelectionInProgress = true;
        SelectionAction action = new SelectionAction(isCancellable);
        for(SelectionListener<T> listener : _listeners)
        {
            listener.selectionPerformed(currValue, newValue, action);
        }
        _isSelectionInProgress = false;
        
        return action.isCancelled();
    }
    
    public boolean isSelectionInProgress()
    {
        return _isSelectionInProgress;
    }

    /**
     * Finds the object stored in this model that is the <strong>exact</strong> same instance of {@code obj}. If none is
     * found, {@code null} is returned.
     *
     * @param obj
     * @return
     */
    private T findMatchingObject(Object obj)
    {
        T match = null;

        for(T elem : this.getElements())
        {
            if(obj == elem)
            {
                match = elem;
                break;
            }
        }

        return match;
    }

    @Override
    public T getSelectedItem()
    {
        return _selectedItem;
    }
}