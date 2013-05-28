[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPlayer player) {
            final MagicPlayer opponent = player.getOpponent();
            return (!opponent.hasState(MagicPlayerState.WasDealtDamage)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    },
    new MagicWhenDamageIsDealtTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicPlayer player = permanent.getController();
            final MagicPlayer opponent = player.getOpponent();
            final int amount = damage.getDealtAmount();
            return damage.getTarget() == opponent ?
                new MagicEvent(
                    permanent,
                    amount,
                    this,
                    "PN puts RN +1/+1 counters on SN."
                ):
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
