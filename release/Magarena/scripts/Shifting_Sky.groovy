[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. All nonland permanents are the chosen color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            game.doAction(new AddStaticAction(event.getPermanent(), 
                new MagicStatic(MagicLayer.Color, NONLAND_PERMANENT) {
                    @Override
                    public int getColorFlags(final MagicPermanent perm, final int flags) {
                        return color.getMask();
                    }
                }
            ));
        }
    }
]
