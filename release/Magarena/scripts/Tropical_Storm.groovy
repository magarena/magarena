[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals "+amount+" damage to each creature with flying and 1 additional damage to each blue creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getCardOnStack().getX();
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),CREATURE_WITH_FLYING);
            final Collection<MagicPermanent> blueTargets=
                game.filterPermanents(event.getPlayer(),BLUE_CREATURE);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(source,target,amount));
            }
            for (final MagicPermanent target : blueTargets) {
                game.doAction(new MagicDealDamageAction(source,target,1));
            }
        }
    }
]
