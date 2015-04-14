[
    new MagicWhenOtherDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent died) {
            return (permanent.getEnchantedPermanent()==died) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a 3/3 green Elephant creature token onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new PlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("3/3 green Elephant creature token")));
        }
    }
]
