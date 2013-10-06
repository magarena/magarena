
def OpalineDraw = new MagicWhenTargetedTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack stack) {
        return (stack.containsInChoiceResults(permanent) &&
                stack.isSpell() &&
                permanent.isEnemy(stack)) ?
            new MagicEvent(
                permanent,
                new MagicSimpleMayChoice(
                    MagicSimpleMayChoice.DRAW_CARDS,
                    1,
                    MagicSimpleMayChoice.DEFAULT_NONE
                ),
                this,
                "PN may\$ draw a card."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        if (event.isYes()) {
            game.doAction(new MagicDrawAction(event.getPlayer(),1));
        }
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {    
            permanent.addAbility(OpalineDraw);
        }
    }
]
