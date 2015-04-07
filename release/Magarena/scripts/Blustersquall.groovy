[
    new MagicOverloadActivation(MagicTiming.Tapping) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{U}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Tap each creature\$ you don't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.filterPermanents(event.getPlayer(), MagicTargetFilterFactory.CREATURE_YOUR_OPPONENT_CONTROLS) each {
                game.doAction(new MagicTapAction(it));
            }
        }
    }
]
