[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                this,
                "SN deals 4 damage to target creature\$. " +
                "If seven or more cards are in your graveyard, SN deals 4 damage to that creature's controller as well."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,4));
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    game.doAction(new MagicDealDamageAction(event.getSource(),it.getController(),4));
                }
            });
        }
    }
]
