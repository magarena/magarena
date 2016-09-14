[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Tokens"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{B}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.ANOTHER_CREATURE_YOU_CONTROL)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "PN puts X 2/2 black Zombie creature tokens onto the battlefield, where X is RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getPower();
            game.logAppendValue(player,amount);
            game.doAction(new PlayTokensAction(
                player,
                CardDefinitions.getToken("2/2 black Zombie creature token"),
                amount
            ));
        }
    }
]
