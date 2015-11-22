[
    new SelfLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "Return exiled cards to the battlefield tapped under their owners' control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.doAction(new ReturnLinkedExileAction(
                event.getPermanent(),
                MagicLocationType.Play,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
