[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature or player\$."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,3));
            });
        }
    }
]
