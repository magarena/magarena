package support.ui;

/**
 * Provides a textual description for an item of type {@code T}.
 * 
 * @author jak2
 * @param <T> 
 */
public interface DescriptionProvider<T>
{
    /**
     * The text displayed to represent the item. May not be {@code null}.
     * 
     * @return 
     */
    public String getDisplayText(T item);
    
    /**
     * The text displayed as a tool tip for the item. May be {@code null}.
     * 
     * @return 
     */
    public String getToolTipText(T item);
}