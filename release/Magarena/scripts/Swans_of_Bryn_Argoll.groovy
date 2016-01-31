[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final amount = (damage.getTarget() == permanent) ? damage.prevent() : 0;
            return amount > 0 ?
                new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    amount,
                    this,
                    "PN draws RN cards."
                ):
                MagicEvent.NONE;
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
