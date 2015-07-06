[
    new MagicWouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            if (act.card.hasType(MagicType.Creature) &&
                act.card.isEnemy(permanent) &&
                act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    }
]
