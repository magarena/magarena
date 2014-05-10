[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                MagicColorChoice.ALL_INSTANCE,
                this,
                "Choose a color\$. " +
                "Your opponents can't cast spells of the chosen color."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicColor color = event.getChosenColor();
            game.doAction(new MagicChangePlayerStateAction(
                event.getPlayer().getOpponent(),
                MagicPlayerState.CantCastSpells
            ));
        }
    }
]
