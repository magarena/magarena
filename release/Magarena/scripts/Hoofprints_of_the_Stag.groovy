def YOUR_TURN_CONDITION = new MagicCondition() {
    public boolean accept(final MagicSource source) {
        final MagicGame game = source.getGame();
        return source.getController() == game.getTurnPlayer();
    }
};

[
    new MagicWhenOtherDrawnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCard card) {
            return permanent.isFriend(card) ?
                new MagicEvent(
                    permanent,
                    new MagicSimpleMayChoice(
                        MagicSimpleMayChoice.ADD_POS_COUNTER,
                        1,
                        MagicSimpleMayChoice.DEFAULT_YES
                    ),
                    this,
                    "PN may\$ put a hoofprint counter on SN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeCountersAction(event.getPermanent(),MagicCounterType.Hoofprint,1,true));
            }
        }
    },
    new MagicPermanentActivation(
        [
            YOUR_TURN_CONDITION,
        ],
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{2}{W}"),
                new MagicRemoveCounterEvent(source,MagicCounterType.Hoofprint,4)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a 4/4 white Elemental creature token with flying onto the battlefield."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("4/4 white Elemental creature token with flying")));
        }
    }
]
