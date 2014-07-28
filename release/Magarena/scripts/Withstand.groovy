[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 3 damage that would be dealt to target creature or player\$ this turn. Draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new MagicPreventDamageAction(it,3));
                game.doAction(new MagicDrawAction(event.getPlayer()));
            });
        }
    }
]
