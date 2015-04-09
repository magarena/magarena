[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature\$. " +
                "SN deals 5 damage to that creature instead if seven or more cards are in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(
                    event.getSource(),
                    it,
                    MagicCondition.THRESHOLD_CONDITION.accept(event.getSource()) ? 5 : 3
                ))
            });
        }
    }
]
