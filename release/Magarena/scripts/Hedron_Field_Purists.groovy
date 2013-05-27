[
    new MagicStatic(MagicLayer.SetPT) {
        @Override
        public void modPowerToughness(
                final MagicPermanent source,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
            final int charges = permanent.getCounters(MagicCounterType.Charge);
            if (charges >= 5) {
                pt.set(2,5);
            } else if (charges >= 1) {
                pt.set(1,4);
            }
        }
    },
    new MagicIfDamageWouldBeDealtTrigger(5) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final int amountDamage = damage.getAmount();
            final int amountCounters = permanent.getCounters(MagicCounterType.Charge);
            if (amountCounters > 0 &&
                !damage.isUnpreventable() &&
                amountDamage > 0) {
                if ((damage.getTarget().isPermanent() &&
                    damage.getTarget().isCreature() &&
                    damage.getTarget().getController() == player) ||
                    damage.getTarget() == player) {
                    final int amountPrevented = amountCounters >= 5 ? 2:1;
                    // Prevention effect.
                    damage.setAmount(amountDamage - amountPrevented);
                }
            }            
            return MagicEvent.NONE;
        }
    }
]
