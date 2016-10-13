[
    new ThisCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}{G}")),
                    MagicColorChoice.MOST_INSTANCE
                ),
                this,
                "PN may\$ pay {2}{G}\$. If PN does, he or she chooses a color\$, "+
                "then creates a 1/1 green Saproling creature token for each permanent of that color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicPlayer player=event.getPlayer();
                final MagicColor color=event.getChosenColor();
                PERMANENT.filter(event) each {
                    if (it.hasColor(color)) {
                        game.doAction(new PlayTokenAction(player,CardDefinitions.getToken("1/1 green Saproling creature token")));
                    }
                }
            }
        }
    }
]
