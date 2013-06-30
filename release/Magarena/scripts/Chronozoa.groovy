[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return permanent.getCounters(MagicCounterType.Charge) == 0 ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts two tokens that are copies of SN onto the battlefield."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokensAction(
                event.getPlayer(),
                event.getPermanent().getCardDefinition(),
                2
            ));
        }
    }
]
