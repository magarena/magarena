[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return new MagicEvent(
                permanent,
                enchanted.getController(),
                enchanted,
                this,
                "PN sacrifices RN and ${permanent.getController().getName()} puts " +
                "a 1/1 colorless Myr artifact creature token onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new SacrificeAction(event.getRefPermanent()));
            game.doAction(new PlayTokenAction(
                    event.getPermanent().getController(),
                    CardDefinitions.getToken("1/1 colorless Myr artifact creature token")
                ));
        }
    }
]
