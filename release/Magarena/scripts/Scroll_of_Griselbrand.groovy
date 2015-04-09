[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_OPPONENT,
                this,
                "Target opponent\$ discards a card. If PN controls a Demon, that player loses 3 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.addEvent(new MagicDiscardEvent(
                    event.getSource(),
                    it
                ));
                if (it.controlsPermanent(MagicSubType.Demon)) {
                    game.doAction(new MagicChangeLifeAction(it,-3));
                }
            });
        }
    }
]
