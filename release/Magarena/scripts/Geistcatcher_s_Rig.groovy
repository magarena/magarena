[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.NEG_TARGET_CREATURE_WITH_FLYING),
                new MagicDamageTargetPicker(4),
                this,
                "PN may\$ have SN deal 4 damage " +
                "to target creature\$ with flying."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTargetPermanent(game,new MagicPermanentAction() {
                    public void doAction(final MagicPermanent creature) {
                        final MagicDamage damage = new MagicDamage(event.getPermanent(),creature,4);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }
    }
]
