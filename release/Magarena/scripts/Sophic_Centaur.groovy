[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Life"
    ) {

        @Override
        public Iterable getCostEvent(final MagicPermanent source) {
            return [
            new MagicPayManaCostEvent(source,"{2}{G}{G}"),
            new MagicTapEvent(source),
            new MagicDiscardEvent(source,1)
         ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 2 life for each card in his or her hand."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            final int amount = player.getHandSize()*2;
            game.doAction(new ChangeLifeAction(player, amount));
            game.logAppendMessage(player, "${player.getName()} gains (${amount}) life.");
        }
    }
]
