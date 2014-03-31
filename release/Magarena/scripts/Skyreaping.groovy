[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals damage to each creature with flying equal to PN's devotion to green."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getDevotion(MagicColor.Green);
            final MagicSource source = event.getSource();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_WITH_FLYING);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(source,target,amount);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
