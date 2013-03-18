[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
                game.doAction(new MagicSetAbilityAction(permanent,MagicAbility.Haste));
            }
            return MagicEvent.NONE;
        }
    }
]
