[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            final int amount = permanent.getController().getDevotion(MagicColor.Red);
            new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals damage to target creature\$ equal the number of red mana symbols in the mana costs of permanents PN controls."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game,new MagicTargetAction() {
                public void doAction(final MagicTarget target) {
                    final MagicDamage damage=new MagicDamage(event.getSource(),target,event.getRefInt());
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
