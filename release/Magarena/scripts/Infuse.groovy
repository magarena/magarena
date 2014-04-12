def ARTIFACT_CREATURE_OR_LAND = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isLand() || target.isCreature() || target.isArtifact();
    }
};

def TARGET_ARTIFACT_CREATURE_OR_LAND = new MagicTargetChoice(
    ARTIFACT_CREATURE_OR_LAND,
    "target artifact, creature or land"
);
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_ARTIFACT_CREATURE_OR_LAND,
                this,
                "PN untaps target artifact, creature, or land.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame outerGame, final MagicEvent outerEvent) {
            MagicPlayer outerPlayer = outerEvent.getPlayer();
            MagicSource outerSource = outerEvent.getSource();
            outerEvent.processTargetPermanent(outerGame, {
                MagicPermanent permanent ->
                outerGame.doAction(new MagicUntapAction(permanent));
            });
            outerGame.doAction(new MagicAddTriggerAction(new MagicAtUpkeepTrigger() {
                @Override
                public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
                    new MagicEvent(
                        outerSource,
                        outerPlayer,
                        this,
                        "PN draws a card"
                    );
                }

                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    game.doAction(new MagicDrawAction(outerPlayer));
                    game.doAction(new MagicRemoveTriggerAction(this));
                }
            }));         
        }
    }
]
