package magic.model;

import magic.model.event.MagicActivation;
import magic.model.target.MagicTarget;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.mstatic.MagicPermanentStatic;

import java.util.Collection;
import java.util.Collections;

public class MagicCard implements MagicSource,MagicTarget,Comparable<MagicCard> {

    public static final MagicCard NONE = new MagicCard(MagicCardDefinition.UNKNOWN, MagicPlayer.NONE, 0) {
        @Override
        public MagicCard copy(final MagicCopyMap copyMap) {
            return this;
        }
    };

    private static final int TOKEN_ID=-1;
    
    private final MagicCardDefinition cardDefinition;
    private final MagicPlayer owner;
    private final long id;
    private final int imageIndex;
    private boolean token=false;
    private boolean known=true;
    
    public MagicCard(final MagicCardDefinition aCardDefinition,final MagicPlayer aOwner,final long aId) {
        cardDefinition = aCardDefinition;
        owner = aOwner;
        id = aId;
        imageIndex = (int)Math.abs(id % 1000);
    }

    private MagicCard(final MagicCopyMap copyMap, final MagicCard sourceCard) {
        copyMap.put(sourceCard, this);
    
        cardDefinition = sourceCard.cardDefinition;
        owner = copyMap.copy(sourceCard.owner);
        id = sourceCard.id;
        imageIndex = (int)Math.abs(id % 1000);
        token = sourceCard.token;
        known = sourceCard.known;
    }
        
    @Override
    public MagicCard copy(final MagicCopyMap copyMap) {
        return new MagicCard(copyMap, this); 
    }
    
    @Override
    public MagicCard map(final MagicGame game) {
        final MagicPlayer mappedOwner=owner.map(game);
        MagicCard card = MagicCard.NONE;
        if (card == MagicCard.NONE) {
            card = mappedOwner.getHand().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getGraveyard().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getExile().getCard(id);
        }
        if (card == MagicCard.NONE) {
            card = mappedOwner.getLibrary().getCard(id);
        }
        if (card == MagicCard.NONE) {
            throw new RuntimeException("Mapping card failed, card " + getName() + " " + id + " not found");
        }
        return card;
    }
    
    public long getId() {
        return id;
    }
    
    public int getImageIndex() {
        return imageIndex;
    }

    public MagicCardDefinition getCardDefinition() {
        if (known) {
            return cardDefinition;
        } else {
            return MagicCardDefinition.UNKNOWN;
        }
    }
        
    public MagicPlayer getOwner() {
        return owner;
    }
            
    private void setToken() {
        token = true;
    }
    
    public boolean isToken() {
        return token;
    }
    
    public int getPower() {
        return genPowerToughness().power();
    }
    
    public int getToughness() {
        return genPowerToughness().toughness();
    }    
    
    public MagicPowerToughness genPowerToughness() {
        return genPowerToughness(MagicPermanent.NONE);
    }

    public MagicPowerToughness genPowerToughness(final MagicPermanent perm) {
        final MagicPowerToughness pt = cardDefinition.genCardPowerToughness();
        cardDefinition.applyCDAPowerToughness(
                getGame(), 
                perm.isValid() ? perm.getController() : getOwner(),
                perm,
                pt);
        return pt;
    }
    
    public MagicManaCost getCost() {
        final MagicManaCost cost = getCardDefinition().getCost();
        final MagicGame game = getGame();

        //cost modifications due to continous effects
        for (final MagicPermanentStatic mpstatic : game.getStatics(MagicLayer.Game)) {
            final MagicStatic mstatic = mpstatic.getStatic();
            final MagicPermanent source = mpstatic.getPermanent();
            /*
            if (mstatic.accept(game, source, this)) {
               mstatic.modManaCost(soure, this, cost);
            }
            */
        }

        return cost;
    }
    
    public static MagicCard createTokenCard(final MagicCardDefinition cardDefinition,final MagicPlayer owner) {
        final MagicCard card=new MagicCard(cardDefinition,owner,MagicCard.TOKEN_ID);    
        card.setToken();
        return card;
    }

    void setKnown(final boolean known) {
        this.known=known;
    }
    
    public boolean isKnown() {
        return known;
    }
    
    @Override
    public String getName() {
        return getCardDefinition().getName();
    }
    
    @Override
    public String toString() {
        return getName();
    }

    @Override
    public MagicPlayer getController() {
        return owner;
    }

    @Override
    public int getPreventDamage() {
        return 0;
    }

    @Override
    public void setPreventDamage(final int amount) {
        
    }
    
    @Override
    public boolean isValidTarget(final MagicSource source) {
        return true;
    }

    @Override
    public boolean isPermanent() {
        return false;
    }
    
    @Override
    public boolean isCreature() {
        return false;
    }

    @Override
    public boolean isPlayer() {
        return false;
    }
    
    @Override
    public boolean isSpell() {
        return true;
    }
    
    @Override
    public MagicColoredType getColoredType() {
        return getCardDefinition().getColoredType();
    }

    @Override
    public int getColorFlags() {
        return getCardDefinition().getColorFlags();
    }
    
    @Override
    public boolean hasAbility(final MagicAbility ability) {
        return getCardDefinition().hasAbility(ability);
    }
    
    @Override
    public Collection<MagicActivation> getActivations() {
        return Collections.singletonList((MagicActivation)cardDefinition.getCardActivation());
    }

    @Override
    public int compareTo(final MagicCard card) {
        return Long.signum(id - card.id);
    }
    
    @Override
    public MagicGame getGame() {
        return owner.getGame();
    }
}
