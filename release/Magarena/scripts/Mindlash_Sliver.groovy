
def MindlashSacrifice = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Draw),
    "Discard"
) {

    @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicPayManaCostEvent(source,"{1}"),
            new MagicSacrificeEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Each player discards a card."
        );
    }

    @Override
     public void executeEvent(final MagicGame game, final MagicEvent event) {
        for (final MagicPlayer player : game.getPlayers()) {
            game.addEvent(new MagicDiscardEvent(
                event.getSource(),
                player,
                1
            ));
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
            permanent.addAbility(MindlashSacrifice);
        }
    }
]
