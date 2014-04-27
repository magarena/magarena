[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals X ("+amount+") damage to target creature or player.\$ " +
                "That creature can't be regenerated this turn. If the creature would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                if (target.isCreature()) {
                    final MagicPermanent creature = (MagicPermanent)target;
                    game.doAction(new MagicAddTurnTriggerAction(creature,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
                    game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                };
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
