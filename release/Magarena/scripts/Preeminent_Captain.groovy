def SOLDIER_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Creature) &&
               target.hasSubType(MagicSubType.Soldier);
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def A_SOLDIER_CARD_FROM_HAND = new MagicTargetChoice(
    SOLDIER_CARD_FROM_HAND,  
    MagicTargetHint.None,
    "a Soldier creature card from your hand"
);
[
    new MagicWhenAttacksTrigger(1) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return permanent == attacker ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN may put a Soldier creature card from his or her hand onto the battlefield tapped and attacking."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put a Soldier card onto the battlefield?",
                    A_SOLDIER_CARD_FROM_HAND,
                ),
                [MagicPlayMod.TAPPED,MagicPlayMod.ATTACKING]
            ));
        }
    }
]
