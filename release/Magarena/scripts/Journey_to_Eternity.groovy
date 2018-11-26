[
    new OtherDiesTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicPermanent died) {
            return permanent.getEnchantedPermanent() == died;
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                permanent.getEnchantedPermanent(),
                this,
                "PN returns RN to the battlefield under PN's control, " +
                "then returns SN to the battlefield transformed under PN's control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PutOntoBattlefieldAction(
                MagicLocationType.Graveyard,
                event.getRefPermanent().getCard(),
                event.getPlayer()
            ));
            game.doAction(new PutOntoBattlefieldAction(
                MagicLocationType.Graveyard,
                event.getPermanent().getCard(),
                event.getPlayer(),
                MagicPlayMod.TRANSFORMED
            ));
        }
    }
]

