[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isOpponent(eotPlayer) && !permanent.getController().hasState(MagicPlayerState.HasLostLife) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(),
                    this,
                    "PN may\$ put a quest counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                game.doAction(new ChangeCountersAction(event.getPlayer(), event.getPermanent(), MagicCounterType.Quest,1));
            }
        }
    }
]
