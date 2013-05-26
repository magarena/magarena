[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicKickerChoice(
                    MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER, 
                    MagicManaCost.create("{1}{R}"), 
                    true, 
                    true
                ),
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        target,
                        1
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
