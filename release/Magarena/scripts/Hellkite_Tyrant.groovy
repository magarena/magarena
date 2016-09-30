[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                permanent.getController(),
                damage.getTargetPlayer(),
                this,
                "PN gains control of all artifacts RN controls."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicPlayer that = event.getRefPlayer();
            ARTIFACT_YOU_CONTROL.filter(that) each {
                game.doAction(new GainControlAction(player, it));
            }
        }
    }
]

