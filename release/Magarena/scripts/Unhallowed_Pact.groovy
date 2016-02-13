[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return (enchanted == died) ?
                new MagicEvent(
                    permanent,
                    enchanted.getCard(),
                    this,
                    "PN returns RN to the battlefield under his or her control."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getRefCard();
            game.doAction(new ReanimateAction(
                card,
                event.getPlayer()
            ));
        }
    }
]
