[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = CREATURE_CARD_FROM_GRAVEYARD.filter(permanent).size();
            if (amount>0) {
                game.doAction(new ChangeCountersAction(permanent, MagicCounterType.PlusOne, amount));
            } 
            return MagicEvent.NONE;
        }
    },
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "Exile all creature cards from PN's graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_CARD_FROM_GRAVEYARD.filter(event) each {
                game.doAction(new RemoveCardAction(it, MagicLocationType.Graveyard));
                game.doAction(new MoveCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
            }
        }
    }
]
