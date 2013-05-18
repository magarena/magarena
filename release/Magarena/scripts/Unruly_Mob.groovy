[
    new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPlayer player = permanent.getController();
            return (permanent != otherPermanent &&
                    otherPermanent.isCreature() &&
                    otherPermanent.getController() == player) ?
                new MagicEvent(
                    permanent,
                    player,
                    this,
                    "Put a +1/+1 counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                1,
                true
            ));
        }
    }
]
