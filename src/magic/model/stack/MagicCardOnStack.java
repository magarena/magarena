package magic.model.stack;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCopyMap;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.event.MagicActivation;
import magic.model.event.MagicEvent;

import javax.swing.ImageIcon;
import java.util.Arrays;
import java.util.Collection;

public class MagicCardOnStack extends MagicItemOnStack implements MagicSource {

    private MagicLocationType moveLocation=MagicLocationType.Graveyard;
    private int x;
    
    public MagicCardOnStack(final MagicCard card,final MagicPlayer controller,final MagicPayedCost payedCost) {
        setSource(card);
        setController(controller);
        setEvent(card.getCardDefinition().getCardEvent().getEvent(this,payedCost));
        x=payedCost.getX();
    }
    
    public MagicCardOnStack(final MagicCard card,final MagicPayedCost payedCost) {
        this(card,card.getController(),payedCost);
    }
    
    private MagicCardOnStack(final MagicCopyMap copyMap, final MagicCardOnStack cardOnStack) {
        super(copyMap, cardOnStack);
        moveLocation = cardOnStack.moveLocation;
        x = cardOnStack.x;
    }
    
    public MagicCardOnStack copyCardOnStack(final MagicPlayer player) {
        final MagicPayedCost cost=new MagicPayedCost();
        cost.setX(x);
        final MagicCard card=MagicCard.createTokenCard(getCardDefinition(),player);
        final MagicCardOnStack copyCardOnStack=new MagicCardOnStack(card,cost);
        final Object[] choiceResults=getChoiceResults();
        if (choiceResults!=null) {
            copyCardOnStack.setChoiceResults(Arrays.copyOf(choiceResults,choiceResults.length));
        }
        return copyCardOnStack;
    }
    
    @Override
    public MagicCardOnStack copy(final MagicCopyMap copyMap) {
        return new MagicCardOnStack(copyMap, this);
    }
    
    @Override
    public MagicCardOnStack map(final MagicGame game) {
        return (MagicCardOnStack)super.map(game);
    }
    
    public MagicCard getCard() {
        return (MagicCard)getSource();
    }
    
    public void setMoveLocation(final MagicLocationType moveLocation) {
        this.moveLocation=moveLocation;
    }
    
    public MagicLocationType getMoveLocation() {
        return moveLocation;
    }
    
    public int getConvertedCost() {
        return getCardDefinition().getConvertedCost()+x;
    }

    public int getX() {
        return x;
    }

    @Override
    public boolean isSpell() {
        return true;
    }
    
    @Override
    public boolean canBeCountered() {
        return !getCardDefinition().hasAbility(MagicAbility.CannotBeCountered);
    }
    
    @Override
    public ImageIcon getIcon() {
        return getCard().getCardDefinition().getIcon();
    }

    @Override
    public MagicGame getGame() {
        return getSource().getGame();
    }
    
    @Override
    public Collection<MagicActivation> getActivations() {
        return getSource().getActivations();
    }
    
    @Override
    public int getColorFlags() {
        return getSource().getColorFlags();
    }
    
    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return getSource().hasAbility(ability);
    }
    
    @Override
    public boolean isCreature() {
        return getSource().isCreature();
    }

    @Override
    public MagicEvent[] getCostEvent(final MagicActivation act) {
        return getSource().getCostEvent(act);
    }
}
