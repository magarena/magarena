[
    new MagicCardAbilityActivation(
        new MagicActivationHints(MagicTiming.Counter, true), 
        "Channel"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{X}{R}{R}{R}"),
                new MagicDiscardSelfEvent(source)
            ];
        }

        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "SN deals X damage to each creature without flying. (X="+payedCost.getX()+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final Collection<MagicPermanent> creatures = game.filterPermanents(MagicTargetFilterFactory.CREATURE_WITHOUT_FLYING);
            for (final MagicPermanent creature : creatures) {
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,event.getRefInt());
                game.doAction(new MagicDealDamageAction(damage));
            }
        }
    },
    
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{X}{R}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                source,
                MagicTargetChoice.Negative("target creature without flying"),
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals X damage to target creature with flying\$. (X="+amount+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent creature ->
                final MagicDamage damage = new MagicDamage(event.getSource(),creature,event.getRefInt());
                game.doAction(new MagicDealDamageAction(damage));
            });
        }
    }
]
