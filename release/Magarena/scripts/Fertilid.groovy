[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Search"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{G}"),
                new MagicRemoveCounterEvent(source,MagicCounterType.PlusOne,1)
            ];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                POS_TARGET_PLAYER,
                this,
                "Target player\$ searches his or her library for a basic land card and puts it " +
                "onto the battlefield tapped. Then that player shuffles his or her library."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event,
                    new MagicFromCardFilterChoice(
                        BASIC_LAND_CARD_FROM_LIBRARY,
                        1,
                        true,
                        "to put onto the battlefield tapped"
                    ),
                    MagicPlayMod.TAPPED
                ));
            });
        }
    }
]
