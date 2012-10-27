package support.ui;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * Uses a {@link DescriptionProvider} to determine what is rendered for each cell. Delegates the actual rendering.
 *
 * @param <T> The type of the data being rendered to a cell.
 *
 * @author jak2
 */
class DescriptionProviderCellRenderer<T> implements ListCellRenderer
{
    interface ItemInfoProvider<T>
    {
        public T getElementDisplayedAt(int i);
    }

    private final DescriptionProvider<T> _descriptionProvider;
    private final ListCellRenderer _delegateRenderer;
    private final Map<T, String> _cachedDisplayText, _cachedTooltips;
    private final ItemInfoProvider<T> _infoProvider;

    /**
     * A ListCellRenderer that uses the {@code descriptionProvider} to determine what is actually rendered by the
     * {@code delegateRenderer}. The {@code delegateRenderer} should be the renderer that would normally
     * be used if this renderer was not used.
     *
     * @param delegateRenderer
     * @param infoProvider
     * @param descriptionProvider
     */
    public DescriptionProviderCellRenderer(ListCellRenderer delegateRenderer, ItemInfoProvider<T> infoProvider,
            DescriptionProvider<T> descriptionProvider)
    {
        _delegateRenderer = delegateRenderer;
        _infoProvider = infoProvider;
        _descriptionProvider = descriptionProvider;
        
        _cachedDisplayText = new HashMap<T, String>();
        _cachedTooltips = new HashMap<T, String>();
    }

    /**
     * Clears the cached Strings built using the supplied {@link StringConverter}. The cache is used to increase
     * performance, but the cache will need to be cleared when updated conversions are desired.
     */
    public void clearCache()
    {
        _cachedDisplayText.clear();
        _cachedTooltips.clear();
    }

    /**
     * Renders the cell using the {@code #_delegateRenderer} provided to the constructor. Intercepts the object that the
     * {@code #_delegateRenderer} would normally have rendered and instead has it render the String supplied by the 
     * {@code #_descriptionProvider} provided to the constructor.
     *
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value,
            int index, boolean isSelected, boolean cellHasFocus)
    {
        T valueInModel = _infoProvider.getElementDisplayedAt(index);
        
        if(!_cachedDisplayText.containsKey(valueInModel))
        {
            _cachedDisplayText.put(valueInModel, _descriptionProvider.getDisplayText(valueInModel));
        }
        String displayText = _cachedDisplayText.get(valueInModel);
        
        Component comp = _delegateRenderer.getListCellRendererComponent(list, displayText, index, isSelected,
                cellHasFocus);
        
        //JComponent has a setToolTipText(String) method but Component does not
        if(comp instanceof JComponent)
        {
            if(!_cachedTooltips.containsKey(valueInModel))
            {
                String tooltip = _descriptionProvider.getToolTipText(valueInModel);
                
                final int wrapLength = 80;
                if(tooltip != null && tooltip.length() > wrapLength)
                {
                    StringBuilder wrappedTooltip = new StringBuilder("<html>");
                    int lineCounter = 0;
                    for(char c : tooltip.toCharArray())
                    {
                        lineCounter++;
                        
                        if(lineCounter > wrapLength && c == ' ')
                        {
                            wrappedTooltip.append("<br/>");
                            lineCounter = 0;
                        }
                        else
                        {
                            wrappedTooltip.append(c);
                        }
                    }
                    wrappedTooltip.append("</html>");
                    
                    tooltip = wrappedTooltip.toString();
                }
                
                _cachedTooltips.put(valueInModel, tooltip);
            }
            String tooltip = _cachedTooltips.get(valueInModel);
            
            ((JComponent) comp).setToolTipText(tooltip);
        }
        
        return comp;
    }
}