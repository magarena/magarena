[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.hasColor(MagicColor.Red)) ?
                new MagicEvent(
                    permanent,
                    MagicTargetChoice.NEG_TARGET_CREATURE,
                    new MagicNoCombatTargetPicker(false,true,false),
                    this,
                    "Target creature\$ can't block this turn."
                ) :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicGainAbilityAction(
                    permanent,
                    MagicAbility.CannotBlock
                ));
            });
        }
    }
]
