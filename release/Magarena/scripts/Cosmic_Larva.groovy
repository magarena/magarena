[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice("Sacrifice two lands?"),
                    this,
                    "PN may\$ sacrifice two lands. If PN doesn't, sacrifice SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getNrOfPermanents(MagicType.Land) >=2 && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_LAND));
			game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),MagicTargetChoice.SACRIFICE_LAND));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    }
]
