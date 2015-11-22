[
    new SelfLeavesBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final RemoveFromPlayAction act) {
            return new MagicEvent(
                permanent,
                this,
                "PN gains life equal to the number of age counters on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Age);
            game.doAction(new ChangeLifeAction(
                event.getPlayer(),
                amount
            ));
        }
    }
]
