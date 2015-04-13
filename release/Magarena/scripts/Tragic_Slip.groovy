[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                new MagicWeakenTargetPicker(1,1),
                this,
                "Target creature\$ gets -1/-1 until end of turn. " +
                "That creature gets -13/-13 until end of turn instead if a creature died this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = game.getCreatureDiedThisTurn() ? -13 : -1;
                game.doAction(new ChangeTurnPTAction(it,amount,amount));
            });
        }
    }
]
