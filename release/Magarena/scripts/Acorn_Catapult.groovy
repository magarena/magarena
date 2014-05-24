[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{1}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.TARGET_CREATURE_OR_PLAYER,
                new MagicDamageTargetPicker(1),
                this,
                "SN deals 1 damage to target creature or player\$. " +
                "That creature's controller or that player puts a 1/1 " +
                "green Squirrel creature token onto the battlefield"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTarget(game, {
                final MagicTarget target ->
                final MagicDamage damage = new MagicDamage(
                    event.getSource(),
                    target,
                    1
                );
                game.doAction(new MagicDealDamageAction(damage));
                game.doAction(new MagicPlayTokenAction(
                    target.getController(),
                    TokenCardDefinitions.get("1/1 green Squirrel creature token")
                ));
            });
        }
    }
]
