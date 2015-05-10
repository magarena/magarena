[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}{G}")),
                    MagicColorChoice.MOST_INSTANCE
                ),
                this,
                "You may\$ pay {2}{G}\$. If you do, choose a color\$. "+
                "Put a 1/1 green Saproling creature token onto the battlefield for each permanent of that color."
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
