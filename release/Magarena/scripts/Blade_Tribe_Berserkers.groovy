[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPayedCost payedCost) {
            if (MagicCondition.METALCRAFT_CONDITION.accept(permanent)) {
                game.doAction(new MagicChangeTurnPTAction(permanent,3,3));
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Haste));
            }
            return MagicEvent.NONE;
        }
    }
]
