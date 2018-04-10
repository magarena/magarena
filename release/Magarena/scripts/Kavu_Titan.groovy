[
    new EntersWithCounterTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPayedCost payedCost) {
            if (payedCost.isKicked()) {
                game.doAction(new ChangeCountersAction(permanent.getController(),permanent,MagicCounterType.PlusOne,3));
                game.doAction(new GainAbilityAction(permanent,MagicAbility.Trample,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    }
]
