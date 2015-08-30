[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE_OR_PLAYER,
                MagicPreventTargetPicker.create(),
                this,
                "Prevent the next 2 damage that would be dealt to target creature or player\$ this turn. "+
                "If SN was kicked, prevent the next 4 damage instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.isKicked() ? 4 : 2;
                game.doAction(new PreventDamageAction(it,amount));
            });
        }
    }
]
