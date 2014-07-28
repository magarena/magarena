[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(4),
                this,
                "SN deals 4 damage to target player\$ and each creature he or she controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                MagicDamage damage = new MagicDamage(event.getSource(), it, 4);
                game.doAction(new MagicDealDamageAction(damage));
                final Collection<MagicPermanent> targets = game.filterPermanents(
                        it,
                        MagicTargetFilterFactory.CREATURE_YOU_CONTROL);
                for (final MagicPermanent target : targets) {
                    damage = new MagicDamage(event.getSource(), target, 4);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
