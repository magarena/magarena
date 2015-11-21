[
    new AtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each creature each opponent controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOUR_OPPONENT_CONTROLS.filter(event) each {
                game.doAction(new ChangeCountersAction(it, MagicCounterType.PlusOne, 1));
            }
        }
    }
]
