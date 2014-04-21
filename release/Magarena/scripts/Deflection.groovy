def SINGLE_TARGET_SPELL = new MagicStackFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicItemOnStack target) {
        // we need something here to establish singleTarget and multiTarget Conditions
        final MagicTargetChoice tchoice = target.getEvent().getTargetChoice();
        return target.isSpell() && tchoice.isValid() && tchoice.isTargeted();
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
                game.addFirstEvent(new MagicEvent( 
                    event.getSource(),
                    targetSpell.getEvent().getTargetChoice(),
                    targetSpell,
                    ChangeTargetAction,
                    ""
                ));
            });
        }
    }
]
