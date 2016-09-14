[
    new MagicOverloadActivation(MagicTiming.Removal) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                MagicPayManaCostEvent.Cast(source,"{X}{X}{R}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            final int x = payedCost.getX();
            return new MagicEvent(
                cardOnStack,
                x,
                this,
                "SN deals " + x + " damage to each creature\$ without flying PN doesn't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicSource source = event.getSource();
            final int amount = event.getRefInt();
            CREATURE_WITHOUT_FLYING_YOUR_OPPONENT_CONTROLS.filter(event) each {
                game.doAction(new DealDamageAction(source,it,amount));
            }
        }
    }
]
