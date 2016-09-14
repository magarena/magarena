[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int X = source.getGame().getNrOfPermanents(MagicSubType.Goblin);
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(X),
                this,
                "SN deals X damage to target creature\$ and X damage to PN, "+
                "where X is the number of Goblins on the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final int X = game.getNrOfPermanents(MagicSubType.Goblin);
                final MagicSource source = event.getSource();
                game.doAction(new DealDamageAction(source,it,X));
                game.doAction(new DealDamageAction(source,event.getPlayer(),X));
            });
        }
    }
]
