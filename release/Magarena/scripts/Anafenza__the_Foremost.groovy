[
    new LeavesBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            final MagicPermanent it = act.getPermanent();
            if (it.isNonToken() &&
                it.isCreature() &&
                it.getCard().isEnemy(permanent) &&
                act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    },
    new WouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            if (act.card.hasType(MagicType.Creature) &&
                act.card.isToken() == false &&
                act.from(MagicLocationType.Battlefield) == false &&
                act.card.isEnemy(permanent) &&
                act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    }
]
