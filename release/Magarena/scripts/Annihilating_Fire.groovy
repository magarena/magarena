[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature or player\$. " + 
                "If a creature dealt damage this way would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),target,3);
                    game.doAction(new MagicDealDamageAction(damage));
                    if (target.isCreature()) {
                        game.doAction(new MagicAddTurnTriggerAction(
                            (MagicPermanent)target, 
                            MagicWhenLeavesPlayTrigger.IfDieExileInstead
                        ));
                    }
                }
            });
        }
    }
]
