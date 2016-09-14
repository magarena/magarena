[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Reanimate"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{3}{W}{B}{B}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Return a creature card named Deathpact Angel from PN's graveyard to the battlefield under his or her control."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            CARD_FROM_GRAVEYARD.filter(event) find {
                if (it.getName().equals("Deathpact Angel")) {
                    game.doAction(new ReanimateAction(it, event.getPlayer()));
                    return true;
                }
            }
        }
    }
]
