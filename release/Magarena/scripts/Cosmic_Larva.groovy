[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Sacrifice two lands?"),
                this,
                "PN may\$ sacrifice two lands. If PN doesn't, sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes() && event.getPlayer().getNrOfPermanents(MagicType.Land) >= 2) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_LAND));
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_LAND));
            } else {
                game.doAction(new SacrificeAction(event.getPermanent()));
            }
        }
    }
]
