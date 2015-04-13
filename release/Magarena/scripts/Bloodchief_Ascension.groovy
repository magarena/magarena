[
    new MagicWhenOtherPutIntoGraveyardTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicMoveCardAction act) {
            final MagicCard card = act.card;
            return (card.isEnemy(permanent) &&
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
            if (event.isYes()){
                game.doAction(new ChangeLifeAction(event.getPlayer().getOpponent(),-2));
                game.doAction(new ChangeLifeAction(event.getPlayer(),2));
            }
        }
    }
]
