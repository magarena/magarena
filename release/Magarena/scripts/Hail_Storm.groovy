[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 2 damage to each attacking creature and 1 damage to PN and each creature PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final MagicSource source = event.getSource();
            final Collection<MagicPermanent> targetsAttacking =
                game.filterPermanents(player,MagicTargetFilterFactory.ATTACKING_CREATURE);
            final Collection<MagicPermanent> targetsCreatures =
                game.filterPermanents(player,MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targetsAttacking) {
                game.doAction(new MagicDealDamageAction(source,target,2));
            }
            game.doAction(new MagicDealDamageAction(source,player,1));
            for (final MagicPermanent target : targetsCreatures) {
                game.doAction(new MagicDealDamageAction(source,target,1));
            }
        }
    }
]
