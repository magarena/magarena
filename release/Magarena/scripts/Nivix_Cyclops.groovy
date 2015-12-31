[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    (cardOnStack.hasType(MagicType.Instant) ||
                    cardOnStack.hasType(MagicType.Sorcery))) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN gets +3/+0 until end of turn and can attack this turn as though it didn't have defender."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new ChangeTurnPTAction(event.getPermanent(), 3, 0));
            game.doAction(new GainAbilityAction(event.getPermanent(), MagicAbility.CanAttackWithDefender));
        }
    }
]
