[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 5 damage to each creature without flying if you have 7 or more cards in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                CREATURE_WITHOUT_FLYING.filter(game) each {
                    game.doAction(new DealDamageAction(event.getSource(),it,5));
                }
            }
        }
    }
]
