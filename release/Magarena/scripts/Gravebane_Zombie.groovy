[
    new WouldBeMovedTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MoveCardAction act) {
            final MagicPermanent dying = act.permanent;
            if (permanent == dying &&
                act.to(MagicLocationType.Graveyard) &&
                act.from(MagicLocationType.Battlefield)) {
                act.setToLocation(MagicLocationType.TopOfOwnersLibrary);
                game.logAppendMessage(permanent.getController(), "${dying.getName()} is put on top of ${dying.getOwner()}'s library.");
            }
            return MagicEvent.NONE;
        }
    }
]
