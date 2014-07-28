[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getNrOfPermanents(MagicSubType.Human);
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target player\$ " +
                "equal to the number of Humans PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Human);
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
