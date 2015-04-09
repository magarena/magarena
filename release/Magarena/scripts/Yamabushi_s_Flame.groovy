[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(3),
                this,
                "SN deals 3 damage to target creature or player\$. "+
                "If a creature dealt damage this way would die this turn, exile it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            int dealtDamage = 0;
            event.processTarget(game, {
                final MagicDamage damage=new MagicDamage(event.getSource(),it,3);
                game.doAction(new MagicDealDamageAction(damage));
                dealtDamage = damage.getDealtAmount();
            });
            event.processTargetPermanent(game, {
                if (dealtDamage > 0) {
                    game.doAction(new MagicAddTurnTriggerAction(
                        it,
                        MagicWhenSelfLeavesPlayTrigger.IfDieExileInstead
                    ));
                }
            });
        }
    }
]
