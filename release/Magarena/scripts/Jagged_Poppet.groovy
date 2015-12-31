[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            return damage.getTarget() == permanent ?
                new MagicEvent(
                    permanent,
                    damage.getDealtAmount(),
                    this,
                    "PN discards RN cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer(), event.getRefInt()));
        }
    },

    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return MagicCondition.HELLBENT.accept(permanent) ?
                new MagicEvent (
                    permanent,
                    damage.getTargetPlayer(),
                    damage.getDealtAmount(),
                    this,
                    "PN discards RN cards."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (MagicCondition.HELLBENT.accept(event.getPermanent())) {
                game.addEvent(new MagicDiscardEvent(event.getPermanent(), event.getPlayer(), event.getRefInt()));
            }
        }
    }
]
