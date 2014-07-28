[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature\$. " + 
                "If that creature would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicDamage damage = new MagicDamage(event.getSource(),it,2);
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicAddTurnTriggerAction(
                    it, 
                    MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead
                ));
            });
        }
    }
]
