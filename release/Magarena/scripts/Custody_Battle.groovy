[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted.isController(upkeepPlayer) ?
                new MagicEvent(
                    enchanted,
                    new MagicMayChoice("Sacrifice a land?"),
                    this,
                    "PN may\$ sacrifice a land. If you don't, " +
                    upkeepPlayer.getOpponent() + " gains control of SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().controlsPermanent(MagicType.Land) && event.isYes()) {
                game.addEvent(new MagicSacrificePermanentEvent(event.getPermanent(),event.getPlayer(),SACRIFICE_LAND));
            } else {
                game.doAction(new GainControlAction(
                    event.getPlayer().getOpponent(),
                    event.getPermanent()
                ));
            }
        }
    }
]
