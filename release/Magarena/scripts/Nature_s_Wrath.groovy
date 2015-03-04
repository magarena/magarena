def SWAMP_OR_BLACK_PERMANENT = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Black) || target.hasSubType(MagicSubType.Swamp);
    } 
};

def A_SWAMP_OR_BLACK_PERMANENT = new MagicTargetChoice(
    SWAMP_OR_BLACK_PERMANENT,
    "a Swamp or a black permanent"
);

def ISLAND_OR_BLUE_PERMANENT = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target) {
        return target.hasColor(MagicColor.Black) || target.hasSubType(MagicSubType.Swamp);
    } 
};

def AN_ISLAND_OR_BLUE_PERMANENT = new MagicTargetChoice(
    ISLAND_OR_BLUE_PERMANENT,
    "an Island or a blue permanent"
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
                A_SWAMP_OR_BLACK_PERMANENT
            ));        }
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
                AN_ISLAND_OR_BLUE_PERMANENT
            ));
        }
    }
]
