[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_LAND,
                this,
                "Destroy target land\$. " +
                "If seven or more cards are in your graveyard, instead destroy that land and SN deals 1 damage to each creature and each player."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDestroyAction(it));
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    final Collection<MagicPermanent> targets = game.filterPermanents(CREATURE);
                    for (final MagicPermanent target : targets) {
                        game.doAction(new MagicDealDamageAction(event.getSource(),target,1));
                    }
                    for (final MagicPlayer player : game.getAPNAP()) {
                        game.doAction(new MagicDealDamageAction(event.getSource(),player,1));
                    }
                }
            });
        }
    }
]
