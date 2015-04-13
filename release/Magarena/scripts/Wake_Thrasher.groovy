[
    new MagicWhenBecomesUntappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return otherPermanent.isFriend(permanent) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "SN gets +1/+1 until end of turn."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(),1,1));
        }
    }
]
