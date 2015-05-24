[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "PN gains life equal to RN's toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getToughness();
            game.logAppendValue(player,amount);
            game.doAction(new ChangeLifeAction(player, amount));
            if (event.isBuyback()) {
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.OwnersHand));
            }
        }
    }
]
