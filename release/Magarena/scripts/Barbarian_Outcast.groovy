[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(MagicSubType.Swamp) == false;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            game.doAction(new MagicPutStateTriggerOnStackAction(new MagicEvent(
                source,
                {
                    final MagicGame G, final MagicEvent E ->
                    G.doAction(new MagicSacrificeAction(E.getPermanent()));
                },
                "Sacrifice SN."
            )));
        }
    }
]
