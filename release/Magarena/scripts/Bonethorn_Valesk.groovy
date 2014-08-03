[
    new MagicWhenTurnedFaceUpTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return new MagicEvent(
                permanent,
                MagicTargetChoice.NEG_TARGET_CREATURE_OR_PLAYER,
                MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$."
            )
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicDamage damage = new MagicDamage(event.getSource(),it,1);
            game.doAction(new MagicDealDamageAction(damage));
        }
    }
]
