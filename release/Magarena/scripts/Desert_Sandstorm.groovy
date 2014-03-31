[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures =
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage=new MagicDamage(event.getSource(),creature,1);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
