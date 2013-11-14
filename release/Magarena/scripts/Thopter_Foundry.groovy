def TARGET_NONTOKEN_ARTIFACT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               target.isArtifact() &&
               !target.isToken();
    }
};
def SACRIFICE_NONTOKEN_ARTIFACT = new MagicTargetChoice(
    TARGET_NONTOKEN_ARTIFACT_YOU_CONTROL,
    MagicTargetHint.Positive,
    "a nontoken artifact"
);
[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),
        "Token"
    ) {

        @Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [
                new MagicPayManaCostEvent(source,"{1}"),
                new MagicSacrificePermanentEvent(source, SACRIFICE_NONTOKEN_ARTIFACT)
            ];
        }

        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                source.getController(),
                payedCost.getTarget(),
                this,
                "PN a 1/1 blue Thopter artifact creature token with flying onto the battlefield. " + 
                "PN gains 1 life"
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("1/1 blue Thopter artifact creature token with flying")));
            game.doAction(new MagicChangeLifeAction(event.getPlayer(),1));
        }
    }
]
