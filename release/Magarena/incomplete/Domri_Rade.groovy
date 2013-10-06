[
    new MagicPlaneswalkerActivation(1) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Look at the top card of your library. If it's a creature card, you may reveal it and put it into your hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            /////////////////////////////////
        }
    },
    new MagicPlaneswalkerActivation(-2) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.POS_TARGET_PLAYER,
                this,
                "Target creature you control fights another target creature."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            /////////////////////////////////
        }
    },
    new MagicPlaneswalkerActivation(-7) {
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_PLAYER,
                this,
                "PN gets an emblem with \"Creatures you control have double strike, trample, hexproof, and haste.\"."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
                flags.add(MagicAbility.DoubleStrike);
                flags.add(MagicAbility.Trample);
                flags.add(MagicAbility.Hexproof);
                flags.add(MagicAbility.Haste);
            }
        }
    }
]
