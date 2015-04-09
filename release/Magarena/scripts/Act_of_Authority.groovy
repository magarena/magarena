[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    NEG_TARGET_ARTIFACT_OR_ENCHANTMENT
                ),
                MagicExileTargetPicker.create(),
                this,
                "PN may\$ exile target artifact or enchantment\$. "+
                "If PN does, its controller gains control of SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game, {
                    game.addEvent(new MagicExileEvent(it));
                    game.doAction(new MagicGainControlAction(it.getController(),event.getPermanent()));
                });
            }
        }
    }
]
