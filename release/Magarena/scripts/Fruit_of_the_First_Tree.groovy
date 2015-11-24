[
    new OtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEnchantedPermanent() == died) ?
                new MagicEvent(
                    permanent,
                    permanent.getEnchantedPermanent(),
                    this,
                    "PN gains X life and draws X cards, where X is RN's toughness."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int toughness = event.getRefPermanent().getToughness();
                game.doAction(new ChangeLifeAction(event.getPlayer(),toughness));
                game.doAction(new DrawAction(event.getPlayer(),toughness));
        }
    }
]
