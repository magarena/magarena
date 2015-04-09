[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                NEG_TARGET_PLAYER,
                this,
                "SN deals 1 damage to each creature target player\$ " +
                "controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final Collection<MagicPermanent> targets = game.filterPermanents(
                        it,
                        CREATURE_YOU_CONTROL);
                final MagicSource source = event.getSource();
                for (final MagicPermanent target : targets) {
                    game.doAction(new MagicDealDamageAction(source,target,1));
                }
            });
        }
    }
]
