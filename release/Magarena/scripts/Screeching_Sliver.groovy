
def ScreechingMill = new MagicPermanentActivation(
    new MagicActivationHints(MagicTiming.Pump),
    "Mill"
) {

     @Override
    public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
        return [
            new MagicTapEvent(source)
        ];
    }

    @Override
    public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            MagicTargetChoice.TARGET_PLAYER,
            this,
            "Target player\$ puts the top card of " +
            "his or her library into his or her graveyard."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        event.processTargetPlayer(game,new MagicPlayerAction() {
            public void doAction(final MagicPlayer player) {
                game.doAction(new MagicMillLibraryAction(player,1));
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
            permanent.addAbility(ScreechingMill);
        }
    }
]
