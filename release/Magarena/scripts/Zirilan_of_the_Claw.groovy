def DRAGON_PERMANENT_CARD_FROM_LIBRARY = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.hasSubType(MagicSubType.Dragon) && target.isPermanentCard();
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };
def A_DRAGON_PERMANENT_CARD = new MagicTargetChoice(
        DRAGON_PERMANENT_CARD_FROM_LIBRARY,
        MagicTargetHint.None,
        "a Dragon permanent card"
    );

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Search"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{1}{R}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a Dragon permanent card and "+
                "puts that card onto the battlefield. Then shuffles his or her library. "+
                "That Dragon gains haste until end of turn. Exile it at the beginning of "+
                "the next end step."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                A_DRAGON_PERMANENT_CARD,
                [MagicPlayMod.HASTE_UEOT, MagicPlayMod.EXILE_AT_END_OF_TURN]
            ));
        }
    }
]
