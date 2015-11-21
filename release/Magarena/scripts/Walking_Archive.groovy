[
    new AtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final int amount = permanent.getCounters(MagicCounterType.PlusOne)
            return amount >= 1 ?
                new MagicEvent(
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN draws a card for each +1/+1 counter on SN. ("+amount+")"
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.PlusOne);
            if (amount>=1) {
                final MagicPlayer player = event.getPlayer();
                game.doAction(new DrawAction(player,amount));
            }
        }
    }
]
