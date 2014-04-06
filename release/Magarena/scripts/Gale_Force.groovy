[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 5 damage to each creature with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE_WITH_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,5);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
