package support.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A generic list model that does not provide a means for its contents to be mutated.
 *
 * @author jak2
 */
public class GenericImmutableListModel<T> extends GenericAbstractListModel<T>
{
    private final List<T> _elements;

    public GenericImmutableListModel(Iterable<T> data)
    {
        //Implementation note: this code intentionally does not use Google Collection's ImmutableList because it does
        //not support the list containg null

        ArrayList<T> tempList = new ArrayList<T>();
        for(T item : data)
        {
            tempList.add(item);
        }
        _elements = Collections.unmodifiableList(tempList);
    }

    @Override
    public List<T> getElements()
    {
        return _elements;
    }
}