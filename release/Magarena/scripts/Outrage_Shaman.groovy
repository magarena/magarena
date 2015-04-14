[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getDevotion(MagicColor.Red);
            new MagicEvent(
                permanent,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(amount),
                this,
                "SN deals damage to target creature\$ equal the number of red mana symbols in the mana costs of permanents PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final int amount = event.getPlayer().getDevotion(MagicColor.Red);
                game.doAction(new DealDamageAction(event.getSource(),it,amount));
            });
        }
    }]
