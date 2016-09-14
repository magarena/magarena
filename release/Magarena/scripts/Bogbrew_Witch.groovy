def CARD_NAMED_NEWT_OR_CAULDRON = new MagicCardFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
            return target.getName().equals("Festering Newt") || target.getName().equals("Bubbling Cauldron");
        }
        public boolean acceptType(final MagicTargetType targetType) {
            return targetType == MagicTargetType.Library;
        }
    };
def A_CARD_NAMED_NEWT_OR_CAULDRON = new MagicTargetChoice(
        CARD_NAMED_NEWT_OR_CAULDRON,
        MagicTargetHint.None,
        "a card named Festering Newt or Bubbling Cauldron"
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
                new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN searches his or her library for a card named Festering Newt or Bubbling Cauldron, "+
                "puts it onto the battlefield tapped, then shuffles his or her library."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                A_CARD_NAMED_NEWT_OR_CAULDRON,
                MagicPlayMod.TAPPED
            ));
        }
    }
]
