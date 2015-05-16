[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                payedCost.getTarget(),
                this,
                "All creatures get -X/-X until end of turn, where X is RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount=event.getRefPermanent().getPower();
            game.logAppendMessage(event.getPlayer(),"("+amount+")");
            CREATURE.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, -amount, -amount));
            }
        }
    }
]
