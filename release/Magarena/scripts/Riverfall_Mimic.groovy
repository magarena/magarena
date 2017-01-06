def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(3,3);
    }
};

[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.hasColor(MagicColor.Red) &&
                    cardOnStack.hasColor(MagicColor.Blue)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN has base power and toughness 3/3 until end of turn and can't be blocked this turn."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new AddStaticAction(
                event.getPermanent(), PT
            ));
            game.doAction(new GainAbilityAction(
                event.getPermanent(),
                MagicAbility.Unblockable
            ));
        }
    }
]
