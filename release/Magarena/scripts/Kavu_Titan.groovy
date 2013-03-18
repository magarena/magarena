[
    new MagicWhenComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
            final MagicGame game,
            final MagicPermanent permanent,
            final MagicPlayer player) {   
            if (permanent.isKicked()) {
                game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        3,
                        true));
                game.doAction(new MagicSetAbilityAction(permanent,MagicAbility.Trample,MagicStatic.Forever));
            }
            return MagicEvent.NONE;
        }
    }
]
