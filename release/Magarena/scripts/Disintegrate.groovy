[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals RN damage to target creature or player.\$ " +
                "That creature can't be regenerated this turn. If the creature would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
            event.processTargetPermanent(game, {
                game.doAction(ChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                game.doAction(new AddTurnTriggerAction(it,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
            });
        }
    }
]
