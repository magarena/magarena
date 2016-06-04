[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Return to their owners' hands all creatures with toughness less than or equal to the number of Islands PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Island);
            game.logAppendValue(player, amount);
            game.doAction(new RemoveAllFromPlayAction(
                MagicPTTargetFilter.Toughness(
                    CREATURE,
                    Operator.LESS_THAN_OR_EQUAL,
                    amount
                ).filter(event),
                MagicLocationType.OwnersHand
            ));
        }
    }
]
