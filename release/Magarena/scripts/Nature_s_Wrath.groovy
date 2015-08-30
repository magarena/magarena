def SWAMP_OR_BLACK_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               (target.hasSubType(MagicSubType.Swamp) ||
                target.hasColor(MagicColor.Black));
    } 
};

def A_SWAMP_OR_BLACK_PERMANENT_YOU_CONTROL = new MagicTargetChoice(
    SWAMP_OR_BLACK_PERMANENT_YOU_CONTROL,
    "a Swamp or a black permanent to sacrifice"
);

def ISLAND_OR_BLUE_PERMANENT_YOU_CONTROL = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isController(player) &&
               (target.hasSubType(MagicSubType.Island) ||
                target.hasColor(MagicColor.Blue));
    } 
};

def AN_ISLAND_OR_BLUE_PERMANENT_YOU_CONTROL = new MagicTargetChoice(
    ISLAND_OR_BLUE_PERMANENT_YOU_CONTROL,
    "an Island or a blue permanent to sacrifice"
);

[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.hasSubType(MagicSubType.Swamp) || otherPermanent.hasColor(MagicColor.Black) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "PN sacrifices a Swamp or black permanent."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                A_SWAMP_OR_BLACK_PERMANENT_YOU_CONTROL
            ));
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return otherPermanent.hasSubType(MagicSubType.Island) || otherPermanent.hasColor(MagicColor.Blue) ?
                new MagicEvent(
                    permanent,
                    otherPermanent.getController(),
                    this,
                    "PN sacrifices an Island or blue permanent."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificePermanentEvent(
                event.getSource(),
                event.getPlayer(),
                AN_ISLAND_OR_BLUE_PERMANENT_YOU_CONTROL
            ));
        }
    }
]
