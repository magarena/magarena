def AddLand3 = {
    final int pIdx ->
    return new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.incMaxLands();
            game.incMaxLands();
            game.incMaxLands();
        }
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return game.getTurnPlayer().getIndex() == pIdx;
        }
    };
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "PN may play up to three additional lands this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(AddLand3(event.getPlayer().getIndex())));
        }
    }
]
