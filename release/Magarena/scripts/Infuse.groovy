def MagicAtUpkeepTrigger cantrip(final MagicEvent event) {
    return new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                event.getSource(),
                event.getPlayer(),
                {
                    final MagicGame game2, final MagicEvent event2 ->
                    game2.doAction(new MagicDrawAction(event2.getPlayer()));
                    game2.doAction(new MagicRemoveTriggerAction(this));
                },
                "PN draws a card"
            );
        }
    }
}; 

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
                "PN untaps target artifact, creature, or land.\$"+
                "PN draws a card at the beginning of the next turn's upkeep."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                MagicPermanent permanent ->
                game.doAction(new MagicUntapAction(permanent));
            });
            game.doAction(new MagicAddTriggerAction(cantrip(event))); 
        }
    }
]
