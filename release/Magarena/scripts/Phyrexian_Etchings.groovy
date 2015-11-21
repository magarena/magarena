[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer endOfTurnPlayer) {
            return permanent.isController(endOfTurnPlayer) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN draws a card for each age counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPermanent().getCounters(MagicCounterType.Age);
            game.doAction(new DrawAction(event.getPlayer(),amount));
        }
    },
    new WhenSelfDiesTrigger() {     
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {    
            new MagicEvent(
                permanent,
                this,
                "PN loses 2 life for each age counter on SN."
            );
    }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = -2 * event.getPermanent().getCounters(MagicCounterType.Age);
            game.doAction(new ChangeLifeAction(event.getPlayer(),amount));
        }
    }
]
