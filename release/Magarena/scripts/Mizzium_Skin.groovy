[
    new MagicOverloadActivation(MagicTiming.Pump) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{1}{U}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature you control gets +0/+1 and gains hexproof until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, 0, 1));
                game.doAction(new GainAbilityAction(it, MagicAbility.Hexproof));
            }
        }
    }
]
