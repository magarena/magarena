[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                this,
                "SN deals damage to target creature or player\$ equal to the number of creatures PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicType.Creature);
            event.processTarget(game, {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    it,
                    amount
                );
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
