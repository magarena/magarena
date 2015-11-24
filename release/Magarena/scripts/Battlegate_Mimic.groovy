def PT = new MagicStatic(MagicLayer.SetPT, MagicStatic.UntilEOT) {
    @Override
    public void modPowerToughness(final MagicPermanent source,final MagicPermanent permanent,final MagicPowerToughness pt) {
        pt.set(4,2);
    }
};

[
    new OtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {  
            return (permanent.isFriend(cardOnStack) &&
                    cardOnStack.hasColor(MagicColor.Red) && 
                    cardOnStack.hasColor(MagicColor.White)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN becomes 4/2 and gains first strike until end of turn."
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
                MagicAbility.FirstStrike
            ));
        }
    }
]
