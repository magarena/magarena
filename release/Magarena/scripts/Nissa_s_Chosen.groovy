[
    new MagicWhenPutIntoGraveyardTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            act.setToLocation(MagicLocationType.BottomOfOwnersLibrary);
            return MagicEvent.NONE;
        }
    }
]
