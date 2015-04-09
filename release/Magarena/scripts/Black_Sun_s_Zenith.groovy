[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int amount=payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                this,
                "Put "+amount+" -1/-1 counters on each creature. " +
                "Shuffle SN into its owner's library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getCardOnStack().getX();
            game.filterPermanents(event.getPlayer(), CREATURE) each {
                game.doAction(new MagicChangeCountersAction(it, MagicCounterType.MinusOne, amount));
            }
            game.doAction(new MagicChangeCardDestinationAction(event.getCardOnStack(),MagicLocationType.OwnersLibrary));
        }
    }
]
