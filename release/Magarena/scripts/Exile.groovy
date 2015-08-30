def NONWHITE_ATTACKING_CREATURE=new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return !target.hasColor(MagicColor.White) && target.isAttacking() && target.isCreature();
    } 
};

def TARGET_NONWHITE_ATTACKING_CREATURE = new MagicTargetChoice(
    NONWHITE_ATTACKING_CREATURE,
    "target nonwhite attacking creature"
);

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                TARGET_NONWHITE_ATTACKING_CREATURE,
                MagicExileTargetPicker.create(),
                this,
                "Exile target nonwhite attacking creature\$. You gain life equal to its toughness."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new RemoveFromPlayAction(it,MagicLocationType.Exile));
                game.doAction(new ChangeLifeAction(
                    event.getPlayer(),
                    it.getToughness()
                ));
            });
        }
    }
]
