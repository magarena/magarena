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
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            game.doAction(new MagicChangeCountersAction(
                permanent,
                MagicCounterType.Age,
                1
            ));
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Pay cumulative upkeep?"),
                this,
                "PN may\$ lose 1 life for each Age counter on SN. " +
                "If he or she doesn't, sacrifice SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicChangeLifeAction(event.getPlayer(),-event.getPermanent().getCounters(MagicCounterType.Age)));
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
            }
        }
    },
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
