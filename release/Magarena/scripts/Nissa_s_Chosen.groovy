[
    new ThisPutIntoGraveyardTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            act.setToLocation(MagicLocationType.BottomOfOwnersLibrary);
            return MagicEvent.NONE;
        }
    }
]
