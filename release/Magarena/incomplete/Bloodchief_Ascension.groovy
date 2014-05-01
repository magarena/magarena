[
    new MagicAtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer eotPlayer) {
            return (eotPlayer == permanent.getController().getOpponent() &&
                    eotPlayer.lifeGained() <= -2) ? //Is this possible?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_POS_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a quest counter on SN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(
                    event.getPermanent(),
                    MagicCounterType.Quest,
                    1,
                    true
                ));
            }
        }
    },
    
    new MagicWhenOtherPutIntoGraveyardTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicMoveCardAction act) {
            return act.getToLocation() == MagicLocationType.Graveyard;
        }
    
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) &&
                    act.to(MagicLocationType.Graveyard) &&
                    permanent.getCounters(MagicCounterType.Quest) >= 3) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.OPPONENT_LOSE_LIFE,
                        2,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ have "+card.getOwner()+" lose 2 life. If PN does, PN gains 2 life."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes){
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),2));
                game.doAction(new MagicChangeLifeAction(event.getPlayer().getOpponent(),-2));
            }
        }
    }
]
