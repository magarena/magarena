[
    new MagicOverloadActivation(MagicTiming.Removal) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{R}{R}{R}")
            ];
        }
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 4 damage to each creature\$ you don't control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets=
                game.filterPermanents(event.getPlayer(),MagicTargetFilter.TARGET_CREATURE_YOUR_OPPONENT_CONTROLS);
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,4);
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    }
]
