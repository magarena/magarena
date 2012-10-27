package support.ui;

import java.util.List;
import javax.swing.ListModel;

/**
 * A generic version {@link ListModel} with some additional functionality.
 *
 * @author jak2
 */
public interface GenericListModel<T> extends ListModel
{
    /**
     * Returns an immutable list of all of the elements in the model.
     *
     * @return
     */
    public List<T> getElements();

    /**
     * If the model has any elements.
     *
     * @return
     */
    public boolean hasElements();

    /**
     * Overrides this method in {@code ListModel} to always return an element of type {@code T}.
     * 
     * @return
     */
    @Override
    public T getElementAt(int i);

    /**
     * Fires off an event that all of the elements of this model have changed. This will result in the UI using this
     * model to repaint the elements.
     */
    public void notifyRefresh();
}