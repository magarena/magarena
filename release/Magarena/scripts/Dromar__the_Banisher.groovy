[
    new MagicWhenSelfCombatDamagePlayerTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(
                    new MagicPayManaCostChoice(MagicManaCost.create("{2}{U}")),
                    MagicColorChoice.UNSUMMON_INSTANCE
                ),
                this,
                "You may\$ pay {2}{U}\$. If you do, choose a color\$. " +
                "Return all creatures of that color to their owner's hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                final MagicColor color=event.getChosenColor();
                final Collection<MagicPermanent> targets=game.filterPermanents(event.getPlayer(),CREATURE);
                for (final MagicPermanent creature : targets) {
                    if (creature.hasColor(color)) {
                        game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.OwnersHand));
                    }
                }
            }
        }
    }
]
