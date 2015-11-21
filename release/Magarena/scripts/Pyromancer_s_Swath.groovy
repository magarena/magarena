[
    new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.INCREASE_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.getSource().isInstantOrSorcerySpell() &&
                damage.getSource().isFriend(permanent)) {
                damage.setAmount(damage.getAmount() + 2);
            }
            return MagicEvent.NONE;
        }
    },
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
            return new MagicDiscardHandEvent(permanent);
        }
    }
]
