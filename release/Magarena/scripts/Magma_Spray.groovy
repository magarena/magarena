[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature\$. " + 
                "If that creature would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,2));
                game.doAction(new MagicAddTurnTriggerAction(it,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
            });
        }
    }
]
