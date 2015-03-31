[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "SN deals damage to target player\$ equal to the number of artifacts PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicType.Artifact);
            event.processTarget(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
