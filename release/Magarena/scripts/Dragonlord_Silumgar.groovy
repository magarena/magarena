def CREATURE_OR_PLANESWALKER = new MagicPermanentFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicPermanent target) {
        return target.isCreature() || target.isPlaneswalker();
    }
};

def TARGET_CREATURE_OR_PLANESWALKER = new MagicTargetChoice(
    CREATURE_OR_PLANESWALKER,
    MagicTargetHint.Negative,
    "target creature or planeswalker"
);
[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                TARGET_CREATURE_OR_PLANESWALKER,
                MagicExileTargetPicker.create(),
                this,
                "Gain control of target creature or planeswalker\$ for as long as PN controls SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                game.doAction(new AddStaticAction(
                    event.getPermanent(),
                    MagicStatic.ControlAsLongAsYouControlSource(
                        event.getPlayer(),
                        it
                    )
                ));
            });
        }
    }
]
