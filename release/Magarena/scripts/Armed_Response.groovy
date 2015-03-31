[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_ATTACKING_CREATURE,
                this,
                "SN deals damage to target attacking creature\$ equal to the" +
                "number of Equipment PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getNrOfPermanents(MagicSubType.Equipment);
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
