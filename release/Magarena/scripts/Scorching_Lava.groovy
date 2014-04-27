[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature or player\$. "+
                "If SN was kicked, that creature can't be regenerated this turn and if it would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                if (target.isCreature()) {
                    final MagicPermanent creature = (MagicPermanent)target
                    if (event.isKicked()){
                        game.doAction(new MagicAddTurnTriggerAction(creature,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
                        game.doAction(MagicChangeStateAction.Set(creature,MagicPermanentState.CannotBeRegenerated));
                    }
                };
                final MagicDamage damage=new MagicDamage(event.getSource(),target,2);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
