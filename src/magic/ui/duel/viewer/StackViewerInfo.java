package magic.ui.duel.viewer;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

import javax.swing.ImageIcon;

public class StackViewerInfo {

    public final MagicItemOnStack itemOnStack;
    public final MagicCardDefinition cardDefinition;
    public final String name;
    public final ImageIcon icon;
    public final String description;
    public final boolean visible;

    public StackViewerInfo(final MagicGame game,final MagicItemOnStack itemOnStack) {
        this.itemOnStack=itemOnStack;
        cardDefinition=itemOnStack.getSource().getCardDefinition();
        name=itemOnStack.getName();
        icon=itemOnStack.getIcon();
        description=itemOnStack.getDescription();
        visible=itemOnStack.getController()==game.getVisiblePlayer();
    }
}
