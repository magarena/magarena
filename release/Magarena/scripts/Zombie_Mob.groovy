[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            final int amount = permanent.getController().filterCards(MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD).size();
            if (amount>0) {
                game.doAction(new MagicChangeCountersAction(permanent, MagicCounterType.PlusOne, amount));
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
            final MagicPlayer player = event.getPlayer();
            game.filterCards(player, MagicTargetFilterFactory.CREATURE_CARD_FROM_GRAVEYARD) each {
                game.doAction(new MagicRemoveCardAction(it, MagicLocationType.Graveyard));
                game.doAction(new MagicMoveCardAction(it, MagicLocationType.Graveyard, MagicLocationType.Exile));
            }
        }
    }
]
