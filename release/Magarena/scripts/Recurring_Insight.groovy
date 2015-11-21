[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "PN draws cards equal to the number of cards in target opponent's hand.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = it.getHandSize();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new DrawAction(event.getPlayer(), amount));
                final MagicCardOnStack spell = event.getCardOnStack();
                if (spell.getFromLocation() == MagicLocationType.OwnersHand) {
                    game.doAction(new ChangeCardDestinationAction(spell, MagicLocationType.Exile));
                    game.logAppendMessage(event.getPlayer()," Rebound.");
                    game.doAction(new AddTriggerAction(new ReboundTrigger(spell.getCard())));
                }
            });
        }
    }
]
