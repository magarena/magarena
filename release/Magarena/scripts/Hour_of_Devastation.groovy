[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "All creatures lose indestructible until end of turn. " +
                "SN deals 5 damage to each creature and each non-Bolas planeswalker."
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicPlayer player : game.getPlayers()) {
            for (final MagicPermanent permanent : player.getPermanents()) {
                if (permanent.hasType(MagicType.Creature)) {
                    game.doAction(new LoseAbilityAction(permanent, MagicAbility.Indestructible));
                }
            }}

            for (final MagicPlayer player : game.getPlayers()) {
            for (final MagicPermanent permanent : player.getPermanents()) {
                if (permanent.hasType(MagicType.Creature)
                    || (permanent.hasType(MagicType.Planeswalker) && !permanent.hasSubType(MagicSubType.Bolas))) {

                    game.doAction(new DealDamageAction(event.getSource(), permanent, 5));
                }
            }}
        }
    }
]

