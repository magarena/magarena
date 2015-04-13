[
    new MagicComesIntoPlayWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new ChangeCountersAction(permanent,MagicCounterType.PlusOne,2));
                game.doAction(new MagicGainAbilityAction(permanent,MagicAbility.Haste,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    }
]
