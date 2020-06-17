[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            // If a source would deal damage to another Dinosaur you control, prevent all but 1 of that damage.
            return super.accept(permanent, damage) &&
                damage.getTarget().hasSubType(MagicSubType.Dinosaur) &&
                !permanent.equals(damage.getTarget()) &&
                permanent.isFriend(damage.getTarget());
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            damage.prevent(damage.getAmount() - 1);
            return MagicEvent.NONE;
        }
    }
]

