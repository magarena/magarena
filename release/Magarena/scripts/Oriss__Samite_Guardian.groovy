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
        public void executeEvent(final MagicGame outerGame, final MagicEvent event) {
            event.processTargetPlayer(outerGame, {
                final MagicPlayer outerPlayer ->
                outerGame.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Player, MagicStatic.UntilEOT) {
                    @Override
                    public void modPlayer(final MagicPermanent source, final MagicPlayer player) {
                        if (player.getId() == outerPlayer.getId()) {
                            player.setState(MagicPlayerState.CantCastSpells);
                        }
                    }
                }));
                outerGame.doAction(new AddStaticAction(new MagicStatic(MagicLayer.Game, MagicStatic.UntilEOT) {
                    @Override
                    public void modGame(final MagicPermanent source, final MagicGame game) {
                        final MagicPlayer p = outerPlayer.map(game);
                        CREATURE_YOU_CONTROL.filter(p) each {
                            it.addAbility(MagicAbility.CannotAttack);
                        }
                    }
                }))
            });
        }
    }
]
