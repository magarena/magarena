[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            int amt = 0
            if (damage.getTarget() == permanent) {
                amt = damage.prevent();
            }

            return amt > 0 ?
                new MagicEvent(
                    permanent,
                    damage.getSource().getController(),
                    amt,
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
