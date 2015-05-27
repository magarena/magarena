[
    new MagicCardAbilityActivation(
        [MagicCondition.GRAVEYARD_CONDITION],
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{5}{U}{U}"),
                new MagicExileSelfEvent(source, MagicLocationType.Graveyard)
            ];
        }

        @Override
        public MagicEvent getEvent(final MagicSource source) {
            return new MagicEvent(
                source,
                new MagicEventAction() {
                    @Override
                    public void executeEvent(final MagicGame game, final MagicEvent event) {
                        final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                            MagicCardAbilityActivation.this,
                            getCardEvent(event.getCard(), game.getPayedCost())
                        );
                        game.doAction(new PutItemOnStackAction(abilityOnStack));
                    }
                },
                ""
            );
        }
        
        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card for each color among permanents he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            int amount = 0;
            for (final MagicColor color : MagicColor.values()) {
                if (player.controlsPermanent(color)) {
                    amount++;
                }
            }
            game.logAppendValue(player, amount);
            game.doAction(new DrawAction(event.getPlayer(), amount));
        }
    },
    
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Draw),
        "Draw"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{5}{U}{U}")
            ];
        }

        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN draws a card for each color among permanents he or she controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPlayer player = event.getPlayer();
            int amount = 0;
            for (final MagicColor color : MagicColor.values()) {
                if (player.controlsPermanent(color)) {
                    amount++;
                }
            }
            game.logAppendValue(player, amount);
            game.doAction(new DrawAction(event.getPlayer(), amount));
        }
    }
]
