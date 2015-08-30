def ST = new MagicStatic(MagicLayer.Type, MagicStatic.UntilEOT) {
    @Override
    public void modSubTypeFlags(final MagicPermanent P, final Set<MagicSubType> flags) {
        flags.removeAll(MagicSubType.ALL_CREATURES);
    }
};

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "Creatures PN controls get -2/-0 and lose all creature types until the end of the turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                CREATURE_YOU_CONTROL.filter(it) each {
                    final MagicPermanent creature ->
                    game.doAction(new ChangeTurnPTAction(creature,-2,0));
                    game.doAction(new BecomesCreatureAction(creature,ST));
                }
            });
        }
    }
]
