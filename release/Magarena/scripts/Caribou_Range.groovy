def CARIBOU_TOKEN_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) && target.isCreature() && target.hasSubType(MagicSubType.Caribou) && target.isToken();
    }
};
def SACRIFICE_CARIBOU_TOKEN = new MagicTargetChoice(CARIBOU_TOKEN_YOU_CONTROL,MagicTargetHint.None,"a Caribou token you control")

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Life"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicSacrificePermanentEvent(
                    source,
                    SACRIFICE_CARIBOU_TOKEN
                )
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN gains 1 life."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicChangeLifeAction(event.getPermanent().getController(),1));
        }
    }
]
