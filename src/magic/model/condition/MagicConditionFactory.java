package magic.model.condition;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.event.MagicPermanentActivation;
import magic.model.target.MagicOtherCardTargetFilter;
import magic.model.target.MagicOtherPermanentTargetFilter;
import magic.model.target.MagicTargetFilter;
import magic.model.target.MagicTargetFilterFactory;
import magic.model.target.MagicTargetType;

public class MagicConditionFactory {

    public static MagicCondition CounterAtMost(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(counterType) <= n;
            }
        };
    }

    public static MagicCondition CounterAtLeast(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(counterType) >= n;
            }
        };
    }

    public static MagicCondition EnchantedCounterAtLeast(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getEnchantedPermanent().getCounters(counterType) >= n;
            }
        };
    }

    public static MagicCondition CounterEqual(final MagicCounterType counterType, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return permanent.getCounters(counterType) == n;
            }
        };
    }

    public static MagicCondition ManaCost(String manaCost) {
        return MagicManaCost.create(manaCost).getCondition();
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

    public static MagicCondition HasAbility(final MagicAbility ability) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.hasAbility(ability);
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

    public static MagicCondition LibraryAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLibrary().size() >= n;
            }
        };
    }

    public static MagicCondition GraveyardAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getGraveyard().size() >= n;
            }
        };
    }

    public static MagicCondition DevotionAtLeast(final MagicColor color, final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getDevotion(color) >= n;
            }
        };
    }

    public static MagicCondition EnchantedIs(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return filter.accept(source, source.getController(), permanent.getEnchantedPermanent());
            }
        };
    }

    public static MagicCondition EquippedIs(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return filter.accept(source, source.getController(), permanent.getEquippedCreature());
            }
        };
    }

    public static MagicCondition SelfIs(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                final MagicPermanent permanent = (MagicPermanent)source;
                return filter.accept(source, source.getController(), permanent);
            }
        };
    }

    public static MagicCondition YouEnergyAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getEnergy() >= n;
            }
        };
    }

    public static MagicCondition YouLifeAtLeast(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLife() >= n;
            }
        };
    }

    public static MagicCondition YouLifeOrLess(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLife() <= n;
            }
        };
    }

    public static MagicCondition YouGainLifeOrMore(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getController().getLifeGainThisTurn() >= n;
            }
        };
    }

    public static MagicCondition OpponentGainLifeOrMore(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getOpponent().getLifeGainThisTurn() >= n;
            }
        };
    }

    public static MagicCondition OpponentLoseLifeOrMore(final int n) {
        return new MagicCondition() {
            public boolean accept(final MagicSource source) {
                return source.getOpponent().getLifeLossThisTurn() >= n;
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
                return source.getController().controlsPermanent(source, filter);
            }
        };
    }

    public static MagicCondition YouControlOr(final MagicTargetFilter<MagicPermanent> filter1,final MagicTargetFilter<MagicPermanent> filter2) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().controlsPermanent(source, filter1) || source.getController().controlsPermanent(source, filter2);
            }
        };
    }

    public static MagicCondition PlayerControlsSource(final MagicPlayer player) {
        final long id = player.getId();
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().getId() == id;
            }
        };
    }

    public static MagicCondition YouHaveAtLeast(final MagicTargetFilter<MagicCard> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return filter.filter(source.getController()).size() >= amt;
            }
        };
    }

    public static MagicCondition YouControlAtLeast(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().getNrOfPermanents(source, filter) >= amt;
            }
        };
    }

    public static MagicCondition YouControlAtMost(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().getNrOfPermanents(source, filter) <= amt;
            }
        };
    }

    public static MagicCondition OppControlAtLeast(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getOpponent().getNrOfPermanents(source, filter) >= amt;
            }
        };
    }

    public static MagicCondition YouControlNone(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().getNrOfPermanents(source, filter) == 0;
            }
        };
    }

    public static MagicCondition BattlefieldEqual(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getGame().getNrOfPermanents(source, filter) == amt;
            }
        };
    }

    public static MagicCondition BattlefieldAtLeast(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getGame().getNrOfPermanents(source, filter) >= amt;
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

    public static MagicCondition YouControlAnotherAtLeast(final MagicTargetFilter<MagicPermanent> filter, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getController().getNrOfPermanents(
                    new MagicOtherPermanentTargetFilter(filter, (MagicPermanent)source)
                ) >= amt;
            }
        };
    }

    public static MagicCondition OpponentControl(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return source.getOpponent().controlsPermanent(source, filter);
            }
        };
    }

    public static MagicCondition OtherCardInHand(final MagicColor color, final int amt) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                final MagicTargetFilter<MagicCard> filter = new MagicOtherCardTargetFilter(
                    MagicTargetFilterFactory.card(color).from(MagicTargetType.Hand),
                    (MagicCard)source
                );
                final MagicGame game = source.getGame();
                final MagicPlayer player = source.getController();
                return filter.filter(player).size() >= amt;
            }
        };
    }

    public static MagicCondition Unless(final MagicCondition condition) {
        return new MagicCondition() {
            @Override
            public boolean accept(final MagicSource source) {
                return condition.accept(source) == false;
            }
        };
    }
}
