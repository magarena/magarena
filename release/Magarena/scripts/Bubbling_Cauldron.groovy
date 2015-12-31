def choice = new MagicTargetChoice("a creature named Festering Newt to sacrifice");


[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Drain Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{1}"),
                new MagicSacrificePermanentEvent(source, choice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each opponent loses 4 life. PN gains life equal to the life lost this way."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final ChangeLifeAction act = new ChangeLifeAction(player.getOpponent(),-4);
            game.doAction(act);
            game.doAction(new ChangeLifeAction(player,-act.getLifeChange()));
        }
    }

]
