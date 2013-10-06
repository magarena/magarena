
def MindwhipSacrifice = new MagicPermanentActivation(
    [MagicCondition.SORCERY_CONDITION],
    new MagicActivationHints(MagicTiming.Main),
    "Discard"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{2}"),
            new MagicSacrificeEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.TARGET_PLAYER,
            this,
            "Target player\$ discards a card at random."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPlayer(game,new MagicPlayerAction() {
            public void doAction(final MagicPlayer player) {
                game.addEvent(MagicDiscardEvent.Random(event.getPermanent(),player,1));
            }
        });
    }
};

[
    new MagicStatic(
        MagicLayer.Ability,
        MagicTargetFilter.TARGET_SLIVER
    ) {
        @Override
        public void modAbilityFlags(final MagicPermanent source,final MagicPermanent permanent,final Set<MagicAbility> flags) {
            permanent.addAbility(MindwhipSacrifice);
        }
    }
]
