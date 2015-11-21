[
    new IfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicSource source=damage.getSource();
            if (source.isFriend(permanent) &&
                source.hasColor(MagicColor.Red) &&
                source.isInstantOrSorcerySpell()) {
                // Generates no event or action.
                damage.setAmount(damage.getAmount() * 2);
            }
            return MagicEvent.NONE;
        }
    }
]
