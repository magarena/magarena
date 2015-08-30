[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a card?"),
                this,
                "PN may\$ discard a card. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent discard = new MagicDiscardEvent(event.getSource(), event.getPlayer());
            if (event.isYes() && discard.isSatisfied()) {
                game.addEvent(discard);
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
