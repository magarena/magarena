[
    new DamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return damage.isTarget(permanent) ?
                new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    this,
                    "PN gets a poison counter."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeCountersAction(event.getPlayer(), MagicCounterType.Poison, 1));
        }
    }
]
