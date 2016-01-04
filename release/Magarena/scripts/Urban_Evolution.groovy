def AddLand = {
    final int pIdx ->
    return new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
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
                "Draw three cards. PN may play an additional land this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(AddLand(event.getPlayer().getIndex())));
            game.doAction(new DrawAction(event.getPlayer(),3));
        }
    }
]
