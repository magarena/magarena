[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_OPPONENT,
                this,
                "SN deals X damage divided evenly, rounded down, among all creatures target opponent\$ controls. "+
                "(X="+cardOnStack.getX()+")"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final Collection<MagicPermanent> creatures = CREATURE_YOU_CONTROL.filter(it);
                if (creatures.size() > 0) {
                    def damageAmount = event.getCardOnStack().getX() intdiv creatures.size()
                    if (damageAmount > 0) {
                        for (final MagicPermanent creature : creatures) {
                            game.doAction(new DealDamageAction(event.getSource(),creature,damageAmount));
                        }
                    }
                }
            });
        }
    }
]
