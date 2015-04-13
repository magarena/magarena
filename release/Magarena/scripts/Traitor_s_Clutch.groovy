def color = new MagicStatic(MagicLayer.Color, MagicStatic.UntilEOT) {
    @Override
    public int getColorFlags(final MagicPermanent permanent,final int flags) {
        return MagicColor.Black.getMask();
    }
};
[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                POS_TARGET_CREATURE,
                MagicUnblockableTargetPicker.create(),
                this,
                "Target creature\$ gets +1/+0, becomes black, and gains shadow until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new ChangeTurnPTAction(it,1,0));
                game.doAction(new AddStaticAction(it,color));
                game.doAction(new MagicGainAbilityAction(it,MagicAbility.Shadow));
            });
        }
    }
]
