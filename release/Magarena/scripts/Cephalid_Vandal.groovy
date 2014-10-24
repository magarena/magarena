[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "Put the top card of your library into your graveyard for each shred counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicMillLibraryAction(
            event.getPlayer(),
            event.getPermanent().getCounters(MagicCounterType.Shred)
            ));
        }
    }
]
