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
            final Collection<MagicPermanent> targets =
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.ATTACKING_CREATURE);
            final Collection<MagicPermanent> targets2 =
                game.filterPermanents(event.getPlayer(),MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage = new MagicDamage(event.getSource(),target,2);
                game.doAction(new MagicDealDamageAction(damage));
            }
            for (final MagicPermanent target : targets2) {
                final MagicDamage damage2 = new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage2));
            }
            final MagicDamage damage3 = new MagicDamage(event.getSource(),event.getPlayer(),1);
            game.doAction(new MagicDealDamageAction(damage3));
        }
    }
]
