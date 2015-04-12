def GREEN_OR_WHITE_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               (target.hasColor(MagicColor.Green) ||
                target.hasColor(MagicColor.White));
    } 
};

def A_GREEN_OR_WHITE_PERMANENT_YOU_CONTROL = new MagicTargetChoice(
    GREEN_OR_WHITE_PERMANENT_YOU_CONTROL,
    "a green or white permanent to sacrifice"
);

[
    new MagicAtUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.controlsPermanent(MagicColor.Green) || upkeepPlayer.controlsPermanent(MagicColor.White) ?
                new MagicEvent(  
                    permanent,
                    upkeepPlayer,
                    this,
                    "PN sacrifices a green or white permanent."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game,final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                A_GREEN_OR_WHITE_PERMANENT_YOU_CONTROL
            ));
        }   
    }
]
