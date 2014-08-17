def SINGLE_TARGET_SPELL = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        // we need something here to establish singleTarget and multiTarget Conditions
        final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
        return target.isSpell() && tchoice.isValid() && tchoice.isTargeted();
    }
};

def ChangeTargetAction = {
    final MagicGame game, final MagicEvent event ->
    // by pass the legality check which always use the player of the event
    // in this case it should use the controller of the spell
    final MagicTarget target = event.getTarget()
    game.doAction(new MagicChangeTargetAction(event.getRefCardOnStack(), target));
};    

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice(
                    SINGLE_TARGET_SPELL,
                    MagicTargetHint.Negative,
                    "target spell with a single target"
                ),
                this,
                "PN changes the target of target spell with a single target.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                final MagicChoice targetChoice = targetSpell.getEvent().getTargetChoice();
                final MagicTargetFilter<MagicTarget> filter = (MagicTargetFilter<MagicTarget>)targetChoice.getTargetFilter();
                final MagicTargetFilter<MagicTarget> fixedFilter = new MagicFixPlayerTargetFilter(filter, targetSpell.getController());
                final MagicTargetHint hint = targetChoice.getTargetHint(true);
                final String description = targetChoice.getTargetDescription();

                game.addFirstEvent(new MagicEvent( 
                    event.getSource(),
                    new MagicTargetChoice(fixedFilter, hint, description),
                    targetSpell,
                    ChangeTargetAction,
                    ""
                ));
            });
        }
    }
]
