[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                this,
                "PN put a number of +1/+1 counters on SN equal to PN's devotion to green."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent permanent = event.getPermanent();
            game.doAction(new ChangeCountersAction(
                event.getPlayer(),
                permanent,
                MagicCounterType.PlusOne,
                permanent.getController().getDevotion(MagicColor.Green)
            ));
        }
    }
]
