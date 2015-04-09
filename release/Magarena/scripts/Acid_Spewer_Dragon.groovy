[
    new MagicWhenSelfTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                this,
                "PN puts a +1/+1 counter on each other Dragon creature PN controls."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicTargetFilter<MagicPermanent> filter = DRAGON_CREATURE_YOU_CONTROL.except(event.getPermanent())
            game.filterPermanents(event.getPlayer(),filter) each {
                game.doAction(new MagicChangeCountersAction(it,MagicCounterType.PlusOne,1));
            };
        }
    }
]
