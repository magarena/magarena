def ANGEL_OR_DEMON_OR_DRAGON_CARD_FROM_HAND = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasType(MagicType.Creature) &&
               (target.hasSubType(MagicSubType.Angel) ||
               target.hasSubType(MagicSubType.Demon) ||
               target.hasSubType(MagicSubType.Dragon));
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
}; 
def AN_ANGEL_OR_DEMON_OR_DRAGON_CARD_FROM_HAND = new MagicTargetChoice(
    ANGEL_OR_DEMON_OR_DRAGON_CARD_FROM_HAND,  
    MagicTargetHint.None,
    "an Angel, Demon or Dragon creature card from your hand"
);
[
    new ThisAttacksTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent attacker) {
            return new MagicEvent(
                permanent,
                this,
                "PN may put an Angel, Demon or Dragon creature card from his or her hand onto the battlefield tapped and attacking."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicPutOntoBattlefieldEvent(
                event,
                new MagicMayChoice(
                    "Put an Angel, Demon or Dragon card onto the battlefield?",
                    AN_ANGEL_OR_DEMON_OR_DRAGON_CARD_FROM_HAND,
                ),
                [MagicPlayMod.TAPPED,MagicPlayMod.ATTACKING]
            ));
        }
    }
]
