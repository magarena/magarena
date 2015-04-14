[
    new MagicWhenYouCastSpiritOrArcaneTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice Dreamcatcher?"),
                this,
                "PN may\$ sacrifice SN. If PN does, PN draws a card."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new SacrificeAction(event.getPermanent()));
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    }
]
