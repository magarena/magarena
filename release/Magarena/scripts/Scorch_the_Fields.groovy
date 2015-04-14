[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_LAND,
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target land\$. " +
                "SN deals 1 damage to each Human creature."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DestroyAction(it));
                HUMAN_CREATURE.filter(game) each {
                    final MagicPermanent target ->
                    game.doAction(new DealDamageAction(event.getSource(),target,1));
                }
            });
        }
    }
]
