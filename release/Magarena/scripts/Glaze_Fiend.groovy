[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (otherPermanent != permanent &&
                    otherPermanent.isArtifact() &&
                    otherPermanent.getController() == player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "SN gets +2/+2 until end of turn"
                ) :
                MagicEvent.NONE;
        }
        
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            game.doAction(new MagicChangeTurnPTAction(event.getPermanent(),2,2));
        }
    }
]
