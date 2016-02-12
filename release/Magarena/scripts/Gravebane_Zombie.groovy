[
    new LeavesBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            final MagicPermanent dying = act.getPermanent();
            if (permanent == dying && act.to(MagicLocationType.Graveyard)) {
                act.setToLocation(MagicLocationType.TopOfOwnersLibrary);
                game.logAppendMessage(permanent.getController(), "${dying.getName()} is put on top of ${dying.getOwner()}'s library.");
            }
            return MagicEvent.NONE;
        }
    }
]
