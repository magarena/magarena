package support.ui;

/**
 * A listener for changes in selection.
 * 
 * @author jak2
 */
public interface SelectionListener<E>
{
    public static final class SelectionAction
    {
        private final boolean _isCancellable;
        private boolean _cancel = false;
        
        SelectionAction(boolean isCancellable)
        {
            _isCancellable = isCancellable;
        }
        
        public void cancel()
        {
            if(_isCancellable)
            {
                _cancel = true;
            }
        }
        
        public boolean isCancelled()
        {
            return _cancel;
        }
        
        /**
         * If the selection action can be canceled. In some situations, such as the items in the UI component changing,
         * selection will change and there is no way to undo the implicit change in selection that results.
         * 
         * @return 
         */
        public boolean isCancellable()
        {
            return isCancellable();
        }
    }
    
    public void selectionPerformed(E currValue, E newValue, SelectionAction action);
}