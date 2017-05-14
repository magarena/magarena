package magic.ui.duel.viewerinfo;

import javax.swing.ImageIcon;
import magic.data.MagicIcon;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.stack.MagicAbilityOnStack;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.ui.MagicImages;

public class StackItemViewerInfo {

    public final MagicItemOnStack itemOnStack;
    public final MagicCardDefinition cardDefinition;
    public final String name;
    public final ImageIcon icon;
    public final String description;
    public final boolean visible;

    StackItemViewerInfo(final MagicGame game,final MagicItemOnStack itemOnStack) {
        this.itemOnStack=itemOnStack;
        cardDefinition=itemOnStack.getController().isHuman() ?
            itemOnStack.getRealCardDefinition() :
            itemOnStack.getCardDefinition();
        name=itemOnStack.getName();
        description=itemOnStack.getDescription();
        visible=itemOnStack.getController()==game.getVisiblePlayer();
        icon = getIcon(itemOnStack);
    }

    private ImageIcon getIcon(final MagicItemOnStack itemOnStack) {
        if (itemOnStack instanceof MagicAbilityOnStack) {
            return MagicImages.getIcon(MagicIcon.ABILITY);
        } else if (itemOnStack instanceof MagicCardOnStack) {
            return MagicImages.getIcon(itemOnStack.getCardDefinition());
        } else if (itemOnStack instanceof MagicTriggerOnStack) {
            return MagicImages.getIcon(MagicIcon.TRIGGER);
        }
        return null;
    }

    boolean isMagicCard(long magicCardId) {
        if (itemOnStack.isSpell()) {
            return magicCardId == ((MagicCard) itemOnStack.getSource()).getId();
        }
        return false;
    }

}
