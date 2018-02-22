[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            return super.accept(permanent, damage) &&
                permanent.isController(damage.getTarget()) &&
                !damage.isCombat();
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = damage.prevent();
            return new MagicEvent(
                permanent,
                amount,
                this,
                "Prevent RN damage. PN gains RN life."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeLifeAction(event.getPlayer(), event.getRefInt()));
        }
    }
]

