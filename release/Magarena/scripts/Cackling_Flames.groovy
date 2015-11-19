[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals 3 damage to target creature or player\$. " +
                "SN deals 5 damage to that creature or player instead if PN has no cards in hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                game.doAction(new DealDamageAction(
                    event.getSource(),
                    it,
                    MagicCondition.HELLBENT.accept(event.getSource()) ? 5 : 3
                ))
            });
        }
    }
]
