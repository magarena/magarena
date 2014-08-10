[
    new MagicStatic(MagicLayer.Game) {
        @Override
        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
            return source.getController().controlsPermanent(MagicType.Artifact) == false;
        }
        @Override
        public void modGame(final MagicPermanent source, final MagicGame game) {
            final String desc = "Sacrifice Covetous Dragon.";
            if (game.getStack().hasItem(source, desc) == false) {
                game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(new MagicEvent(
                    source,
                    {
                        final MagicGame G, final MagicEvent E ->
                        G.doAction(new MagicSacrificeAction(E.getPermanent()));
                    },
                    desc
                ))));
            }
        }
    }
]
