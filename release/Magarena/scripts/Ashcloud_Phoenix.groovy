[
    new MagicWhenDiesTrigger() {
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
            final MagicCard card = event.getRefCard();
            if (card.isInGraveyard()) {
                game.doAction(new ReanimateAction(
                    card,
                    event.getPlayer(),
                    MagicPlayMod.MORPH
                ));
            }
        }
    }
]
