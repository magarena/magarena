[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Put a shred counter on SN. " + 
                "Then put the top card of PN's library into PN's graveyard for each shred counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.Shred,
                1
            ));
            game.doAction(new MagicMillLibraryAction(
                event.getPlayer(),
                event.getPermanent().getCounters(MagicCounterType.Shred)
            ));
        }
    }
]
