def TP = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent permanent, final Set<MagicSubType> flags) {
        flags.removeAll(MagicSubType.ALL_CREATURES);
    }
};
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_CREATURE,
                MagicPumpTargetPicker.create(),
                this,
                "Target creature\$ gets +3/-3, and loses all creature types until the end of the turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new BecomesCreatureAction(it,TP));
                game.doAction(new ChangeTurnPTAction(it,3,-3));
            });
        }
    }
]
