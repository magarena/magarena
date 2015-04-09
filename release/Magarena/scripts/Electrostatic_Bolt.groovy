[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_CREATURE,
                this,
                "SN deals 2 damage to target creature\$. "+
                "If it's an artifact creature, SN deals 4 damage to it instead."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final int amount = it.isArtifact() && it.isCreature() ? 4 : 2;
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
