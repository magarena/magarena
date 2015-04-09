[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}{W}")),
                    MagicColorChoice.MOST_INSTANCE
                ),
                this,
                "PN may\$ pay {2}{W}\$. If you do, choose a color\$. " +
                "PN gains 1 life for each permanent of that color."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                int life=0;
                final MagicPlayer player=event.getPlayer();
                final MagicColor color=event.getChosenColor();
                final Collection<MagicPermanent> targets=game.filterPermanents(player,PERMANENT);
                for (final MagicPermanent permanent : targets) {
                    if (permanent.hasColor(color)) {
                        life++;
                    }
                }
                game.doAction(new MagicChangeLifeAction(player,life));
            }
        }
    }
]
