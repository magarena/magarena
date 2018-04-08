package magic.model.target;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicColor;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.MagicSubType;
import magic.model.MagicType;
import magic.model.event.MagicEvent;

public abstract class MagicCardFilterImpl implements MagicTargetFilter<MagicCard> {
    @Override
    public List<MagicCard> filter(final MagicSource source) {
        return filter(source, source.getController(), MagicTargetHint.None);
    }

    @Override
    public List<MagicCard> filter(final MagicPlayer player) {
        return filter(MagicSource.NONE, player, MagicTargetHint.None);
    }

    @Override
    public List<MagicCard> filter(final MagicEvent event) {
        return filter(event.getSource(), event.getPlayer(), MagicTargetHint.None);
    }

    @Override
    public List<MagicCard> filter(final MagicSource source, final MagicPlayer player, final MagicTargetHint targetHint) {
        final List<MagicCard> targets = new ArrayList<>();

        // Cards in graveyard
        if (acceptType(MagicTargetType.Graveyard)) {
            add(source, player, player.getGraveyard(), targets, false);
        }

        // Cards in opponent's graveyard
        if (acceptType(MagicTargetType.OpponentsGraveyard)) {
            add(source, player, player.getOpponent().getGraveyard(), targets, false);
        }

        // Cards in opponent's exile
        if (acceptType(MagicTargetType.OpponentsExile)) {
            add(source, player, player.getOpponent().getExile(), targets, false);
        }

        // Cards in hand
        if (acceptType(MagicTargetType.Hand)) {
            add(source, player, player.getHand(), targets, false);
        }

        // Cards in library
        if (acceptType(MagicTargetType.Library)) {
            // only consider unique cards, possible as cards in library will not be counted
            add(source, player, player.getLibrary(), targets, true);
        }

        return targets;
    }

    private void add(final MagicSource source, final MagicPlayer player, final List<MagicCard> cards, final List<MagicCard> targets, final boolean library) {
        final Set<Long> added = new HashSet<>();
        for (final MagicCard card : cards) {
            final boolean old = card.isGameKnown();
            if (library) {
                card.setGameKnown(true);
            }
            if (card.isKnown() && accept(source,player,card) && (library == false || added.contains(card.getStateId()) == false)) {
                targets.add(card);
                added.add(card.getStateId());
            }
            if (library) {
                card.setGameKnown(old);
            }
        }
    }

    @Override
    public boolean acceptType(final MagicTargetType targetType) {
        return false;
    }

    public MagicCardFilterImpl or(final MagicType type) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) || target.hasType(type);
            }
        };
    }
    public MagicCardFilterImpl or(final MagicSubType subType) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) || target.hasSubType(subType);
            }
        };
    }
    public MagicCardFilterImpl or(final MagicColor color) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) || target.hasColor(color);
            }
        };
    }
    public MagicCardFilterImpl or(final MagicAbility ability) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) || target.hasAbility(ability);
            }
        };
    }
    public MagicCardFilterImpl and(final MagicType type) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.hasType(type);
            }
        };
    }
    public MagicCardFilterImpl and(final MagicSubType subType) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.hasSubType(subType);
            }
        };
    }

    /**
     * @param color required color
     * @return return filter with additional condition for target having given color
     */
    public MagicCardFilterImpl and(final MagicColor color) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.hasColor(color);
            }
        };
    }

    /**
     * @param ability required ability
     * @return return filter with additional condition for target having given ability
     */
    public MagicCardFilterImpl and(final MagicAbility ability) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.hasAbility(ability);
            }
        };
    }

    /**
     * @return return filter with additional condition for target being a card that defines a permanent
     */
    public MagicCardFilterImpl permanent() {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.isPermanentCard();
            }
        };
    }

    /**
     * @param n CMC limit
     * @return return filter with additional condition for target's converted mana cost being less or equal to given limit
     */
    public MagicCardFilterImpl cmcLEQ(final int n) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.getConvertedCost() <= n;
            }
        };
    }

    /**
     * @param n power limit
     * @return return filter with additional condition for target's power being less or equal to given limit
     */
    public MagicCardFilterImpl powerLEQ(final int n) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target) && target.getPower() <= n;
            }
        };
    }
    public MagicCardFilterImpl from(final MagicTargetType location) {
        final MagicCardFilterImpl curr = this;
        return new MagicCardFilterImpl() {
            @Override
            public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
                return curr.accept(source, player, target);
            }
            @Override
            public boolean acceptType(final MagicTargetType targetType) {
                return curr.acceptType(targetType) || targetType == location;
            }
        };
    }

    public MagicCardFilterImpl except(final MagicCard invalid) {
        return new MagicOtherCardTargetFilter(this, invalid);
    }
}
