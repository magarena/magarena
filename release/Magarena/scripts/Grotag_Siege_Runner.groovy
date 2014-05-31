[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Destroy"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source, "{R}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target creature with Defender"),
                MagicDestroyTargetPicker.Destroy,
                this,
                "Destroy target creature with defender.\$ SN deals 2 damage to that creature's controller."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.doAction(new MagicDestroyAction(permanent));
                final MagicDamage damage=new MagicDamage(event.getSource(),permanent.getController(),2);
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
