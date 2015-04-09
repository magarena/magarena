[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                this,
                "SN deals damage to target creature\$ equal to the number of Mountains PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Mountain);
            event.processTargetPermanent(game, {
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
