[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 3 damage to target creature\$. " +
                "Raid - SN deals 6 damage to that creature instead if you attacked with a creature this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = (event.getPlayer().getCreaturesAttackedThisTurn() > 0) ? 6 : 3;
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]

