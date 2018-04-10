[
    // Draw and remove hour counters
    new AtDrawTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer drawPlayer) {
            return (permanent.isController(drawPlayer)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws an additional card and remove two hour counters from SN."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(event.getPlayer(), 1));
            game.doAction(new ChangeCountersAction(event.getPlayer(), event.getSource(), MagicCounterType.Hour, -2));
        }
    }
    ,
    // Change maximum hand size
    new MagicStatic(MagicLayer.Game) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            source.getController().setMaxHandSize(source.getCounters(MagicCounterType.Hour));
        }
    }
    ,
    // When discard
    new CardIsDiscardedTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent source, final MagicCard card) {
            return (source.isController(card.getOwner())) ?
                new MagicEvent(
                    source,
                    this,
                    "PN loses 1 life."
                )
                :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), -1));
        }
    }
]

