[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                damage.getTargetPlayer(),
                this,
                "PN sacrifices a creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                A_CREATURE_YOU_CONTROL
            ));
        }
    }
]
