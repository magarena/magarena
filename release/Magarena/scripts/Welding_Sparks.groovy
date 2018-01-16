[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals X damage to target creature\$, " +
                "where X is 3 plus the number of artifacts you control."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = 3 + (int)event.getPlayer().getPermanents().count({ it.hasType(MagicType.Artifact) });
            event.processTarget(game, {
                game.doAction(new DealDamageAction(event.getSource(), it, amount));
            });
        }
    }
]
