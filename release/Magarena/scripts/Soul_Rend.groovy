[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "Destroy target creature\$ if it's white. A creature destroyed this way can't be regenerated. " +
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (it.hasColor(MagicColor.White)) {
                    game.doAction(MagicChangeStateAction.Set(
                        it,
                        MagicPermanentState.CannotBeRegenerated
                    ));
                    game.doAction(new MagicDestroyAction(it));
                };
                game.doAction(new MagicAddTriggerAction(
                    MagicAtUpkeepTrigger.YouDraw(
                        event.getSource(), 
                        event.getPlayer()
                    )
                ));
            });
        }
    }
]
