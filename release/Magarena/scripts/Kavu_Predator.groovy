[
    new MagicWhenLifeIsGainedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicLifeChangeTriggerData lifeChange) {
            final MagicPlayer player = permanent.getController();
            final int amount = lifeChange.amount;
            return (player.getOpponent() == lifeChange.player) ?
                new MagicEvent(
                    permanent,
                    player,
                    amount,
                    this,
                    amount > 1 ?
                        "Put " + amount + " +1/+1 counters on SN." :
                        "Put a +1/+1 counter on SN."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeCountersAction(
                event.getPermanent(),
                MagicCounterType.PlusOne,
                event.getRefInt(),
                true
            ));
        }
    }
]
