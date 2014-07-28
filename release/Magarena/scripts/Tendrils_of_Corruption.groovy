[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                this,
                "SN deals X damage to target creature\$ and PN gains X life, where X is equal to the number of Swamps PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Swamp);
            event.processTargetPermanent(game, {
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    it,
                    amount
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicChangeLifeAction(player,amount));
            });
        }
    }
]
