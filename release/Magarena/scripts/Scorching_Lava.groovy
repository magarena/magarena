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
                game.doAction(new MagicDealDamageAction(event.getSource(),it,2));
            });
            if (event.isKicked()) {
                event.processTargetPermanent(game, {
                    game.doAction(MagicChangeStateAction.Set(it,MagicPermanentState.CannotBeRegenerated));
                    game.doAction(new MagicAddTurnTriggerAction(it,MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead));
                });
            }
        }
    }
]
