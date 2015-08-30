[
    new MagicWouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            if ((act.card.hasType(MagicType.Instant) || act.card.hasType(MagicType.Sorcery)) &&
                act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.Exile);
            }
            return MagicEvent.NONE;
        }
    }
]
