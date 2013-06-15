[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each creature with shadow."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITH_SHADOW);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage = new MagicDamage(event.getSource(),target,2);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
