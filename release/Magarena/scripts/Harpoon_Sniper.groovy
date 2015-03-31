[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{W}"),
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            final int X = source.getController().getNrOfPermanents(MagicSubType.Merfolk);
            return new MagicEvent(
                source,
                MagicTargetChoice.NEG_TARGET_ATTACKING_OR_BLOCKING_CREATURE,
                new MagicDamageTargetPicker(X),
                this,
                "SN deals X damage to target attacking or blocking creature\$, "+
                "where X is the number of Merfolk you control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game,{
                final int X = event.getPlayer().getNrOfPermanents(MagicSubType.Merfolk);
                game.doAction(new MagicDealDamageAction(event.getSource(),it,X));
            });
        }
    }
]
