[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a shred counter on SN. " +
                "Then puts the top card of PN's library into his or her graveyard for each shred counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                event.getPermanent(),
                MagicCounterType.Shred,
                1
            ));
            game.doAction(new MillLibraryAction(
                event.getPlayer(),
                event.getPermanent().getCounters(MagicCounterType.Shred)
            ));
        }
    }
]
