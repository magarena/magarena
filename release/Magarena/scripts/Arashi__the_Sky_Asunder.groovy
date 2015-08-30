[
    new MagicCardAbilityActivation(
        new MagicActivationHints(MagicTiming.Removal, true), 
        "Channel"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
            return [
                new MagicPayManaCostEvent(source, "{X}{G}{G}"),
                new MagicDiscardSelfEvent(source)
            ];
        }

        @Override
        public MagicEvent getCardEvent(final MagicCard source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                payedCost.getX(),
                this,
                "SN deals X damage to each creature with flying. (X="+payedCost.getX()+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CREATURE_WITH_FLYING.filter(event) each {
                game.doAction(new DealDamageAction(event.getSource(), it, event.getRefInt()));
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
                new MagicTapEvent(source), new MagicPayManaCostEvent(source, "{X}{G}")
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int amount = payedCost.getX();
            return new MagicEvent(
                source,
                TARGET_CREATURE_WITH_FLYING,
                new MagicDamageTargetPicker(amount),
                amount,
                this,
                "SN deals X damage to target creature with flying\$. (X="+amount+")"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new DealDamageAction(event.getSource(),it,event.getRefInt()));
            });
        }
    }
]
