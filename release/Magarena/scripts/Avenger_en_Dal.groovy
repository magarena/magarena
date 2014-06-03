[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Exile"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{2}{W}"),
                new MagicDiscardEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                new MagicTargetChoice("target attacking creature"),
                MagicExileTargetPicker.create(),
                this,
                "Exile target attacking creature\$. Its controller gains life equal to its toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent target ->
                game.addEvent(new MagicExileEvent(target));
                game.doAction(new MagicChangeLifeAction(target.getController(),target.getToughness()));
                game.logAppendMessage(event.getPlayer(),"("+target.getToughness()+")");
            });
        }
    }
]
