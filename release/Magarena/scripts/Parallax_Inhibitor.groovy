def PERMANENT_WITH_FADING_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasAbility(MagicAbility.Fading) && target.isController(player);
    } 
};

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Pump),
        "+Counter"
    ) {
        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicTapEvent(source),
                new MagicSacrificeEvent(source)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                this,
                "PN puts a fade counter on each permanent with fading PN controls."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            PERMANENT_WITH_FADING_YOU_CONTROL.filter(event.getPlayer()) each {
                game.doAction(new MagicChangeCountersAction(it, MagicCounterType.Fade, 1));
            }
        }
    }
]
