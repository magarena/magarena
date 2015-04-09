[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_CREATURE,
                new MagicDamageTargetPicker(source.getPower()),
                this,
                "SN deals damage equal to its power to target creature. ("+source.getPower()+")"+
                "That creature deals damage equal to its power to SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent = event.getPermanent();
                final int amount = it.getPower();
                game.logAppendMessage(event.getPlayer()," ("+amount+")");
                game.doAction(new MagicDealDamageAction(permanent,it,permanent.getPower()));
                game.doAction(new MagicDealDamageAction(it,permanent,amount));
            });
        }
    }
]
