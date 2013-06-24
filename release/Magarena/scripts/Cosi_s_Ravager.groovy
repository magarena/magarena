[
    new MagicLandfallTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(MagicTargetChoice.NEG_TARGET_PLAYER),
                new MagicDamageTargetPicker(1),
                this,
                "PN may\$ have SN deal 1 damage to target player\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                event.processTarget(game,new MagicTargetAction() {
                    public void doAction(final MagicTarget target) {
                        final MagicDamage damage = new MagicDamage(event.getPermanent(),target,1);
                        game.doAction(new MagicDealDamageAction(damage));
                    }
                });
            }
        }
    }
]
