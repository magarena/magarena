[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return new MagicEvent(
                permanent,
                enchanted,
                this,
                "RN's controller sacrifices it and PN creates a 1/1 colorless Myr artifact creature token."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getRefPermanent()));
            game.doAction(new PlayTokenAction(
                event.getPlayer(),
                CardDefinitions.getToken("1/1 colorless Myr artifact creature token")
            ));
        }
    }
]
