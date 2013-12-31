[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Prevent"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), 
                new MagicPayManaCostEvent(source, "{G}"), 
                new MagicDiscardEvent(source, 1)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Prevent all combat damage that would be dealt this turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {            
            game.doAction(new MagicAddTurnTriggerAction(
                MagicIfDamageWouldBeDealtTrigger.PreventCombatDamage
            ));            
        }
    }
]
