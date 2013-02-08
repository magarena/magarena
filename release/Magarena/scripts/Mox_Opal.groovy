[
    new MagicManaActivation(
        MagicManaType.ALL_TYPES,
        [MagicCondition.CAN_TAP_CONDITION,
         MagicCondition.METALCRAFT_CONDITION],
        2) {
        @Override
        public MagicEvent[] getCostEvent(final MagicPermanent permanent) {
            return [new MagicTapEvent(permanent)];
        }    
    }
]
