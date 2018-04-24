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
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Target creature PN controls\$ explores. PN may play an additional land this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.addEvent(new MagicExploreEvent(it));
                game.doAction(new AddStaticAction(AddLand(event.getPlayer().getIndex())));
            });
        }
    }
]

