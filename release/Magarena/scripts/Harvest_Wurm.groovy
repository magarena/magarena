def effect = MagicRuleEventAction.create("return a basic land card from your graveyard to your hand.");

[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Return a basic land card from your graveyard to your hand?"),
                this,
                "PN may\$ return a basic land card from his or her graveyard to his or her hand. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent costEvent = effect.getEvent(event);
            if (event.isYes() && costEvent.isSatisfied()) {
                game.addEvent(costEvent);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
