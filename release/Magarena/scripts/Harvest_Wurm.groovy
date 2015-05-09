def effect = MagicRuleEventAction.create("return a basic land card from your graveyard to your hand.");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return a basic land card to your hand?"),
                this,
                "PN may\$ return a basic land card from his or her graveyard to PN's hand. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && BASIC_LAND_CARD_FROM_YOUR_GRAVEYARD.filter(game).size() >= 1) {
                game.addEvent(effect.getEvent(event));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
