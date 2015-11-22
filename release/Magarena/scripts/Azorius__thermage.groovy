[
    new LeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            if (act.getPermanent().isOwner(permanent.getController()) &&
                act.to(MagicLocationType.OwnersHand)) {
                return new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    this,
                    "PN may pay {1}\$. If PN does, PN draws a card."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    }
]
