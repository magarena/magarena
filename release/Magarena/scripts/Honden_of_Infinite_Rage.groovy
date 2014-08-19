[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            final int amt = upkeepPlayer.getNrOfPermanents(MagicSubType.Shrine);
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(amt),
                this,
                "SN deals damage to target creature or player\$ equal to the number of Shrines PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amt = event.getPlayer().getNrOfPermanents(MagicSubType.Shrine);
                final MagicDamage damage=new MagicDamage(event.getSource(),it,amt);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
