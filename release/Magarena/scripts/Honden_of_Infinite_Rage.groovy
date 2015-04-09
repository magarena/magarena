[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final int amt = upkeepPlayer.getNrOfPermanents(MagicSubType.Shrine);
            return new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amt),
                this,
                "SN deals damage to target creature or player\$ equal to the number of Shrines PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
                game.doAction(new MagicDealDamageAction(event.getSource(),it,amount));
            });
        }
    }
]
