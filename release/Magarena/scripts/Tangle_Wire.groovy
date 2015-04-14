def UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isUntapped() && target.isController(player) &&
              (target.isArtifact() || target.isCreature() || target.isLand());
    } 
};

def AN_UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL = new MagicTargetChoice(
    UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL,
    "an untapped artifact, creature or land you control"
);

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                upkeepPlayer,
                this,
                "PN taps an untapped artifact, creature, or land he or she controls for each fade counter on SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            final int n = event.getPermanent().getCounters(MagicCounterType.Fade);
            for (int i=n;i>0;i--) {
                game.addEvent(new MagicTapPermanentEvent(
                    event.getSource(),
                    event.getPlayer(),
                    AN_UNTAPPED_ARTIFACT_CREATURE_OR_LAND_YOU_CONTROL
                ));
            }
        }
    }
]
