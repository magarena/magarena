def SINGLE_TARGET_SPELL = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        return target.isSpell() && target.getEvent().getTargetChoice() != MagicTargetChoice.NONE // we need something here to establish singleTarget and multiTarget Conditions
    }
};

def ChangeTargetAction = {
    final MagicGame game, final MagicEvent event ->
    event.processTarget(game, {
        final MagicTarget target ->
        game.doAction(new MagicChangeTargetAction(event.getRefCardOnStack(), target));
    });
};    

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicTargetChoice(SINGLE_TARGET_SPELL, MagicTargetHint.None, "target spell with a single target"),
                this,
                "PN changes the target of target spell with a single target.\$"
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCardOnStack(game, {
                final MagicCardOnStack targetSpell ->
                final MagicChoice targetChoice = targetSpell.getEvent().getTargetChoice();
                final MagicTargetFilter targetFilter = targetChoice.getTargetFilter();
                final MagicTargetHint targetHint = targetChoice.getTargetHint(true);
                final String targetDescription = targetChoice.getTargetDescription();
                
                game.addFirstEvent(new MagicEvent( 
                    event.getSource(),
                    new MagicTargetChoice(targetFilter, targetHint, targetDescription),
                    targetSpell,
                    ChangeTargetAction,
                    ""
                ));
            });
        }
    }
]
