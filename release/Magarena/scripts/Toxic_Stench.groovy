[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_NONBLACK_CREATURE,
                this,
                "Target nonblack creature\$ gets -1/-1 until end of turn. " +
                "If seven or more cards are in your graveyard, instead destroy that creature. It can't be regenerated."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    game.doAction(ChangeStateAction.Set(
                        it,
                        MagicPermanentState.CannotBeRegenerated
                    ));
                    game.doAction(new MagicDestroyAction(it));
                } else {
                    game.doAction(new MagicChangeTurnPTAction(it,-1,-1));
                }
            });
        }
    }
]
