[
    new MagicOverloadActivation(MagicTiming.Removal) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{2}{U}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "Each creature PN doesn't control gets -4/-0 until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_YOUR_OPPONENT_CONTROLS.filter(event.getPlayer()) each {
                game.doAction(new ChangeTurnPTAction(it, -4, 0));
            }
        }
    }
]
