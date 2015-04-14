[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 1 damage to each creature. "+
                "If a creature dealt damage this way would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> targets = game.filterPermanents(
                event.getPlayer(),
                CREATURE
            );
            for (final MagicPermanent target : targets) {
                final MagicDamage damage=new MagicDamage(event.getSource(),target,1);
                game.doAction(new DealDamageAction(damage));
                if (damage.getDealtAmount() > 0) {
                    game.doAction(new AddTurnTriggerAction(
                        target, 
                        MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead
                    ));
                }
            }
        }
    }
]
