[
    new PreventDamageTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicDamage damage) {
            final int amount = permanent.getCounters(MagicCounterType.Age);
            final int preventAmount = Math.min(amount, damage.getAmount());
            if (damage.getSource().isCreaturePermanent() && damage.isCombat() && damage.getTarget().isFriend(permanent)) {
                // Replacement effect. Generates no event or action.
                damage.prevent(amount);
                game.logAppendMessage(permanent.getController(), "${permanent} prevents (${preventAmount}) damage dealt to (${damage.getTarget()}).");
            }
            return MagicEvent.NONE;
        }
    }
]
