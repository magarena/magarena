[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 4 damage to target creature\$. " +
                "If seven or more cards are in PN's graveyard, instead SN deals 4 damage to that creature and 4 damage to that creature's controller."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,4));
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    game.doAction(new DealDamageAction(event.getSource(),it.getController(),4));
                }
            });
        }
    }
]
