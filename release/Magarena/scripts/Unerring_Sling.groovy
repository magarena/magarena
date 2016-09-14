def tapChoice = new MagicTargetChoice("another untapped creature you control");
def targetChoice = MagicTargetChoice.Negative("target attacking or blocking creature with flying");

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source, "{3}"),
                new MagicTapPermanentEvent(source, tapChoice)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                targetChoice,
                payedCost.getTarget(),
                this,
                "SN deals damage equal to the tapped creature's power to target attacking or blocking creature with flying.\$"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPlayer player = event.getPlayer();
                final int amount=event.getRefPermanent().getPower();
                game.logAppendValue(player,amount);
                game.doAction(new DealDamageAction(event.getPermanent(), it, amount));
            });
        }
    }
]
