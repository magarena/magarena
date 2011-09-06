package magic.ui.viewer;

import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.stack.MagicItemOnStack;

import javax.swing.ImageIcon;

public class StackViewerInfo {

	public MagicItemOnStack itemOnStack;
	public MagicCardDefinition cardDefinition;
	public String name;
	public ImageIcon icon;
	public String description;
	public boolean visible;
	
	public StackViewerInfo(final MagicGame game,final MagicItemOnStack itemOnStack) {
		this.itemOnStack=itemOnStack;
		cardDefinition=itemOnStack.getSource().getCardDefinition();
		name=itemOnStack.getName();
		icon=itemOnStack.getIcon();
		description=itemOnStack.getDescription();
		visible=itemOnStack.getController()==game.getVisiblePlayer();
	}
}
