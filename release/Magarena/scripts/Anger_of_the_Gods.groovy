[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                this,
                "SN deals 3 damage to each creature. " + 
                "If a creature dealt damage this way would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE.filter(event) each {
                final MagicDamage damage = new MagicDamage(event.getSource(), it, 3);
                game.doAction(new DealDamageAction(damage));
                if (damage.getDealtAmount() > 0) {
                    game.doAction(new AddTurnTriggerAction(it, SelfLeavesBattlefieldTrigger.IfDieExileInstead));
                }
            }
        }
    }
]
