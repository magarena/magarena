[
    new MagicIfDamageWouldBeDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (!damage.isUnpreventable() && damage.getTarget() == permanent) {
                damage.setAmount(0);
                return new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    damage.getAmount(),
                    this,
                    "PN draws RN cards."
                );
            }
            return MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicDrawAction(
                event.getPlayer(),
                event.getRefInt()
            ));
        }
    }
]
