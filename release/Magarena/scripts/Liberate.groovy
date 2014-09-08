[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_YOU_CONTROL,
                MagicExileTargetPicker.create(),
                this,
                "Exile target creature\$ you control. Return that card to the battlefield under its owner's control at the beginning of the next end step."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new MagicExileUntilEndOfTurnAction(it));
            });
        }
    }
]
