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
                "SN deals 5 damage to that creature or player instead if a creature died this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final int amount = game.getCreatureDiedThisTurn() ? 5 : 3;
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,amount);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
