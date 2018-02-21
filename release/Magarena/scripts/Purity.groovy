[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.REPLACE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = (permanent.isController(damage.getTarget()) && !damage.isCombat()) ? damage.prevent() : 0;
            if (amount > 0) {
                game.doAction(new ChangeLifeAction(permanent.getController(), amount));
            }
            return MagicEvent.NONE;
        }
    }
]

