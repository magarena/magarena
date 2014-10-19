[
    new MagicWhenComesIntoPlayTrigger(MagicTrigger.REPLACEMENT) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. SN is the chosen color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            final MagicColor color = event.getChosenColor();
            game.doAction(new MagicAddStaticAction(permanent,
                new MagicStatic(MagicLayer.Color,MagicStatic.Forever) {
                @Override
                public int getColorFlags(final MagicPermanent perm, final int flags) {
                    return color.getMask();
                }
            }));
        }

    }
]
