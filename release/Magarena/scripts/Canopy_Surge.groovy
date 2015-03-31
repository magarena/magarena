[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature with flying and each player. " +
                "If SN was kicked, it deals 4 damage to each creature with flying and each player instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.isKicked() ? 4 : 1;
            final Collection<MagicPermanent> targets= game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITH_FLYING);
            for (final MagicPermanent target : targets) {
                game.doAction(new MagicDealDamageAction(event.getSource(),target,amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(event.getSource(),player,amount));
            }
        }
    }
]
