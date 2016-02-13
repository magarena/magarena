[
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                permanent.getCard(),
                this,
                "Return SN to the battlefield face down under PN's control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ReanimateAction(
                event.getRefCard(),
                event.getPlayer(),
                MagicPlayMod.MORPH
            ));
        }
    }
]
