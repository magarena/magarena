[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            final MagicPermanent enchanted = permanent.getEnchantedPermanent();
            return enchanted.isController(upkeepPlayer) ?
                new MagicEvent(
                    enchanted,
                    new MagicMayChoice(
                        MagicTargetChoice.SACRIFICE_LAND
                    ),
                    this,
                    "PN may\$ sacrifice a land\$. If you don't, " +
                    upkeepPlayer.getOpponent() + " gains control of SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.doAction(new MagicSacrificeAction(it));
                });
            } else {
                game.doAction(new MagicGainControlAction(
                    event.getPlayer().getOpponent(),
                    event.getPermanent()
                ));
            }
        }
    }
]
