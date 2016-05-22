[
    new LeavesBattlefieldTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final RemoveFromPlayAction act) {
            final MagicPermanent it = act.getPermanent();
            if (it.isNonToken() &&
                it.isCreature() &&
                it.isEnemy(permanent) &&
                act.to(MagicLocationType.Graveyard)) {

                act.setToLocation(MagicLocationType.Exile);
                game.doAction(new PlayTokenAction(
                    permanent.getController(),
                    CardDefinitions.getToken("2/2 black Zombie creature token")
                ));
            }
            return MagicEvent.NONE;
        }
    }
]
