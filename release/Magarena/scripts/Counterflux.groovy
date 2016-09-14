[
    new MagicOverloadActivation(MagicTiming.Counter) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{1}{U}{U}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Counter each spell PN doesn't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            SPELL_YOU_DONT_CONTROL.filter(event) each {
                game.doAction(new CounterItemOnStackAction(it));
            }
        }
    }
]
