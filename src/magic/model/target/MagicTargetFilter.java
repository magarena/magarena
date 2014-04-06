package magic.model.target;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.EnumSet;
import java.util.HashSet;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCardList;
import magic.model.MagicColor;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicItemOnStack;
import magic.model.choice.MagicTargetChoice;

public interface MagicTargetFilter<T extends MagicTarget> {

    boolean acceptType(final MagicTargetType targetType);

    boolean accept(final MagicGame game,final MagicPlayer player,final T target);

    List<T> filter(final MagicGame game, final MagicPlayer player, final MagicTargetHint targetHint);


    // Permanent reference can not be used because game is copied.
    public static final class MagicOtherCardTargetFilter extends MagicCardFilterImpl {

        private final MagicCardFilterImpl targetFilter;
        private final long id;

        public MagicOtherCardTargetFilter(final MagicCardFilterImpl aTargetFilter,final MagicCard invalidCard) {
            targetFilter = aTargetFilter;
            id = invalidCard.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return targetFilter.accept(game,player,target) &&
                   target.getId() != id;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };
    
    // Permanent reference can not be used because game is copied.
    public static final class MagicPermanentTargetFilter extends MagicPermanentFilterImpl {

        private final long id;

        public MagicPermanentTargetFilter(final MagicPermanent validPermanent) {
            id = validPermanent.getId();
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return target.isPermanent() &&
                   target.getId() == id;
        }
    };

    public static final class MagicPowerTargetFilter extends MagicPermanentFilterImpl {

        private final MagicPermanentFilterImpl targetFilter;
        private final int power;

        public MagicPowerTargetFilter(final MagicPermanentFilterImpl targetFilter,final int power) {
            this.targetFilter = targetFilter;
            this.power = power;
        }
        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return targetFilter.accept(game,player,target) &&
                   target.getPower() <= power;
        }
        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    enum Operator {
        LESS_THAN() {
            public boolean cmp(final int v1, final int v2) {
                return v1 < v2;
            }
        },
        LESS_THAN_OR_EQUAL() {
            public boolean cmp(final int v1, final int v2) {
                return v1 <= v2;
            }
        },
        EQUAL() {
            public boolean cmp(final int v1, final int v2) {
                return v1 == v2;
            }
        };
        public abstract boolean cmp(final int v1, final int v2);
    }

    public static final class MagicCMCCardFilter extends MagicCardFilterImpl {

        private final MagicTargetFilter<MagicCard> targetFilter;
        private final Operator operator;
        private final int cmc;

        public MagicCMCCardFilter(final MagicTargetFilter<MagicCard> targetFilter,final Operator operator,final int cmc) {
            this.targetFilter = targetFilter;
            this.operator = operator;
            this.cmc = cmc;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
            return targetFilter.accept(game,player,target) &&
                   operator.cmp(target.getCardDefinition().getConvertedCost(), cmc) ;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    public static final class MagicCMCPermanentFilter extends MagicPermanentFilterImpl {

        private final MagicTargetFilter<MagicPermanent> targetFilter;
        private final Operator operator;
        private final int cmc;

        public MagicCMCPermanentFilter(final MagicTargetFilter<MagicPermanent> targetFilter,final Operator operator,final int cmc) {
            this.targetFilter = targetFilter;
            this.operator = operator;
            this.cmc = cmc;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return targetFilter.accept(game,player,target) &&
                   operator.cmp(target.getConvertedCost(), cmc) ;
        }

        @Override
        public boolean acceptType(final MagicTargetType targetType) {
            return targetFilter.acceptType(targetType);
        }
    };

    public static final class NameTargetFilter extends MagicPermanentFilterImpl {

        private final String name;
        private final MagicTargetFilter<MagicPermanent> targetFilter;
        
        public NameTargetFilter(final String aName) {
            this(MagicTargetFilterFactory.ANY, aName);
        }

        public NameTargetFilter(final MagicTargetFilter<MagicPermanent> aTargetFilter, final String aName) {
            name = aName;
            targetFilter = aTargetFilter;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName()) && targetFilter.accept(game, player, target);
        }
    };

    public static final class LegendaryCopiesFilter extends MagicPermanentFilterImpl {

        private final String name;

        public LegendaryCopiesFilter(final String name) {
            this.name=name;
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            return name.equals(target.getName()) &&
                   target.hasType(MagicType.Legendary) &&
                   target.isController(player);
        }
    };

    public static final class PlaneswalkerCopiesFilter extends MagicPermanentFilterImpl {

        private final Set<MagicSubType> pwTypes = EnumSet.noneOf(MagicSubType.class);

        public PlaneswalkerCopiesFilter(final MagicPermanent permanent) {
            for (final MagicSubType st : MagicSubType.ALL_PLANESWALKERS) {
                if (permanent.hasSubType(st)) {
                    pwTypes.add(st);
                }
            }
        }

        @Override
        public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
            if (target.hasType(MagicType.Planeswalker) == false) {
                return false;
            }
            if (target.isController(player) == false) {
                return false;
            }
            for (final MagicSubType st : pwTypes) {
                if (target.hasSubType(st)) {
                    return true;
                }
            }
            return false;
        }
    };
}
