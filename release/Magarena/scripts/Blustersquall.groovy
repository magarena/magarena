[
    new MagicOverloadActivation(MagicTiming.Tapping) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{3}{U}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Tap each creature\$ PN doesn't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOUR_OPPONENT_CONTROLS.filter(event) each {
                game.doAction(new TapAction(it));
            }
        }
    }
]
