[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Removal),
        "Damage"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicTapEvent(source),
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "SN deals damage equal to the number of charge counters on it to each creature and each player."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicPermanent source=event.getPermanent();
            final int amount=source.getCounters(MagicCounterType.Charge);
            final Collection<MagicPermanent> creatures=
                game.filterPermanents(event.getPlayer(),CREATURE);
            game.logAppendMessage(event.getPlayer()," ("+amount+")");
            for (final MagicPermanent creature : creatures) {
                game.doAction(new MagicDealDamageAction(source,creature,amount));
            }
            for (final MagicPlayer player : game.getAPNAP()) {
                game.doAction(new MagicDealDamageAction(source,player,amount));
            }
        }
    }
]
