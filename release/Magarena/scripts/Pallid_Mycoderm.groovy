def SACRIFICE_SAPROLING = new MagicTargetChoice("a Saproling to sacrifice");

def FUNGUS_OR_SAPROLING_YOU_CONTROL = new MagicPermanentFilterImpl() {
        public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
            return target.isController(player) &&
                   (target.hasSubType(MagicSubType.Saproling) ||
                   target.hasSubType(MagicSubType.Fungus));
        }
    };

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "Pump"
    ) {

        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicSacrificePermanentEvent(source, SACRIFICE_SAPROLING)];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "Each creature PN controls that's a Fungus or a Saproling gets +1/+1 until end of turn."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            FUNGUS_OR_SAPROLING_YOU_CONTROL.filter(event) each {
                game.doAction(new ChangeTurnPTAction(it, 1,1));
            }
        }
    }
]
