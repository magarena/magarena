[
    new MagicWhenOtherPutIntoGraveyardTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
            return act.getToLocation() == MagicLocationType.Graveyard;
        }
    
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) &&
                    act.to(MagicLocationType.Graveyard)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN puts a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.PlusOne,1));
        }
    }
]
