[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "-Life"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}{B}{G}"),
                new MagicSacrificePermanentEvent(source, MagicTargetChoice.Other("a creature to sacrifice",source))
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getTarget(),
                this,
                "Each opponent loses life equal to RN's power."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount=event.getRefPermanent().getPower();
            game.logAppendMessage(player,"("+amount+")");
            game.doAction(new ChangeLifeAction(player.getOpponent(),-amount));
        }
    }
]
