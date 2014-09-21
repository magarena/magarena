[
    new MagicAtUntapTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer untapPlayer) {
            return permanent.isController(untapPlayer) && untapPlayer.getOpponent().getNrOfPermanents(MagicType.Creature) >= 2 ?
                new MagicEvent(
                    permanent,
                    this,
                    "If an opponent controls 2 or more creatures, SN doesn't untap."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.getPlayer().getOpponent().getNrOfPermanents(MagicType.Creature) >= 2) {
                game.doAction(MagicChangeStateAction.Set(
                    event.getPermanent(),
                    MagicPermanentState.DoesNotUntapDuringNext
                ));
            }
        }
    }
]
