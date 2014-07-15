package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicColor;
import magic.model.MagicCard;
import magic.model.MagicPlayer;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicPermanentActivation;
import magic.model.target.MagicTargetType;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicOtherCardTargetFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;

public class MagicConditionFactory {
    
    public static MagicCondition CounterAtLeast(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(counterType) >= n;
            }
        };
    }

    public static MagicCondition ManaCost(String manaCost) {
        return MagicManaCost.create(manaCost).getCondition();
    }


    public static MagicCondition HasOptions(final MagicTargetChoice targetChoice) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return targetChoice.hasOptions(source.getGame(), source.getController(), source, false);
            }
        };
    }
    
    public static MagicCondition HasSubType(final MagicSubType subtype) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.hasSubType(subtype);
            }
        };
    }
    
    public static MagicCondition NotSubType(final MagicSubType subtype) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.hasSubType(subtype) == false;
            }
        };
    }
    
    public static MagicCondition NoAbility(final MagicAbility ability) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.hasAbility(ability) == false;
            }
        };
    }

    public static MagicCondition HandAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getHandSize() >= n;
            }
        };
    }
    
    public static MagicCondition LifeAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLife() >= n;
            }
        };
    }

    public static MagicCondition SingleActivation(final MagicPermanentActivation act) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                final MagicGame game = source.getGame();
                return !game.getStack().hasActivationOnTop(source,act);
            }
        };
    }
    
    public static MagicCondition YouControl(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().controlsPermanent(filter);
            }
        };
    }
    
    public static MagicCondition YouControlAnother(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().controlsPermanent(
                    new MagicOtherPermanentTargetFilter(filter, (MagicPermanent)source)
                );
            }
        };
    }
    
    public static MagicCondition OpponentControl(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getOpponent().controlsPermanent(filter);
            }
        };
    }
    
    public static MagicCondition OtherCardInHand(final MagicColor color, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                final MagicTargetFilter<MagicCard> filter = new MagicOtherCardTargetFilter(
                    MagicTargetFilterFactory.card(MagicTargetType.Hand, color),
                    (MagicCard)source
                );
                final MagicGame game = source.getGame();
                final MagicPlayer player = source.getController();
                return game.filterCards(player, filter).size() >= amt;
            }
        };
    }
}
