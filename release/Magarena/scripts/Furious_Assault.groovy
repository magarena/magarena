[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (cardOnStack.isFriend(permanent) && cardOnStack.hasType(MagicType.Creature)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_PLAYER,
                    new MagicDamageTargetPicker(1),
                    this,
                    "SN deals 1 damage to target player\$"
            ):
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage = new MagicDamage(event.getSource(),target,1);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
