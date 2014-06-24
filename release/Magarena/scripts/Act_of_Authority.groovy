[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return permanent.isController(upkeepPlayer) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT
                    ),
                    MagicExileTargetPicker.create(),
                    this,
                    "PN may\$ exile target artifact or enchantment\$. "+
                    "If PN does, its controller gains control of SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    final MagicPermanent permanent ->
                    game.addEvent(new MagicExileEvent(permanent));
                    game.doAction(new MagicGainControlAction(permanent.getController(),event.getPermanent()));
                });
            }
        }
    }
]
