[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return permanent.getEnchantedPermanent() == died ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 2/2 white Griffin creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                TokenCardDefinitions.get("2/2 white Griffin creature token with flying")
            ));
        }
    }
]
