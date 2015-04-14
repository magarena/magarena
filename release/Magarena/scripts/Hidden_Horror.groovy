[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a creature card?"),
                this,
                "PN may\$ discard a creature card. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent discard = new MagicDiscardChosenEvent(event.getSource(), A_CREATURE_CARD_FROM_HAND);
            if (event.isYes() && discard.isSatisfied()) {
                game.addEvent(discard);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
