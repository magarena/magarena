[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_SPELL,
                this,
                "Counter target spell\$ that targets a player."
            );
        }
        //FIXME: check if result contains a player NOT targets a player
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game,new MagicCardOnStackAction() {
                public void doAction(final MagicCardOnStack targetSpell) {
                    if (targetSpell.getChoiceResults() != null) {
                        for (final Object choiceResult : targetSpell.getChoiceResults()) {
                            for (final MagicPlayer player : game.getPlayers()) {
                                if (choiceResult == player) {
                                    game.doAction(new MagicCounterItemOnStackAction(targetSpell));
                                    break;
                                }
                            }
                        }
                    }
                }
            });
        }
    }
]
