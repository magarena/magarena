[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
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
                        MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
                for (final MagicPermanent target : targets) {
                    final MagicDamage damage = new MagicDamage(
                        event.getSource(),
                        target,
                        1
                    );
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
