def CARD_NAMED_ORISS = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.getName().equals("Oriss, Samite Guardian");
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Hand;
    }
};

def A_CARD_NAMED_ORISS = new MagicTargetChoice(
    CARD_NAMED_ORISS,
    MagicTargetHint.None,
    "a card named Oriss, Samite Guardian from your hand"
);

[
    new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.FirstMain),
        "Grandeur"
    ) {
        @Override
        public Iterable<? extends MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicDiscardChosenEvent(source,A_CARD_NAMED_ORISS)];
        }
        @Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            return new MagicEvent(
                source,
                NEG_TARGET_PLAYER,
                this,
                "Target player\$ can't cast spells this turn, and creatures that player controls can't attack this turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPlayer(game, {
                game.doAction(new ChangePlayerStateAction(it, MagicPlayerState.CantCastSpells));
                CREATURE_YOU_CONTROL.filter(it) each {
                    final MagicPermanent creature ->
                    game.doAction(new GainAbilityAction(creature, MagicAbility.CannotAttack));
                }
            });
        }
    }
]
