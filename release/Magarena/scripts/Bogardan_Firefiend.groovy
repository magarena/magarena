[
    new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(2),
                this,
                "SN deals 2 damage to target creature\$."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,new MagicPermanentAction() {
                public void doAction(final MagicPermanent creature) {
                    final MagicDamage damage = new MagicDamage(event.getSource(),creature,2);
                    game.doAction(new MagicDealDamageAction(damage));
                }
            });
        }
    }
]
