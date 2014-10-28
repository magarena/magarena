[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature\$. " +
                "SN deals 5 damage to that creature instead if seven or more cards are in your graveyard."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                if (MagicCondition.THRESHOLD_CONDITION.accept(event.getSource())) {
                    final MagicDamage damage5 = new MagicDamage(event.getSource(),it,5);
                    game.doAction(new MagicDealDamageAction(damage5));
                } else {
                    final MagicDamage damage3 = new MagicDamage(event.getSource(),it,3);
                    game.doAction(new MagicDealDamageAction(damage3));
                }
            });
        }
    }
]
