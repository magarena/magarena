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
            if (event.isYes() && event.getPlayer().getHandSize() > 0) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer()));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
