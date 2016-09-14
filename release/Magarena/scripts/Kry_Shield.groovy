[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Block),
        "Pump"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{2}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                TARGET_CREATURE_YOU_CONTROL,
                this,
                "Prevent all damage that would be dealt this turn by target creature PN controls.\$ "+
                "That creature gets +0/+X until end of turn, where X is its converted mana cost."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                final int amount = permanent.getConvertedCost();
                game.logAppendX(event.getPlayer(), amount);
                game.doAction(new AddTurnTriggerAction(
                    permanent,
                    PreventDamageTrigger.PreventDamageDealtBy
                ));
                game.doAction(new ChangeTurnPTAction(permanent, 0, amount));
            });
        }
    }
]
