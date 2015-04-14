[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getTarget() == permanent) {
                final amt = damage.prevent();
                return new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    amt,
                    this,
                    "PN draws RN cards."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new DrawAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
