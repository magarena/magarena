def Duplicate = new AtYourUpkeepTrigger() {
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
        return permanent.isNonToken() ?
            new MagicEvent(
                permanent,
                this,
                "PN creates a token that's a copy of SN."
            ):
            MagicEvent.NONE;
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayTokenAction(
            event.getPlayer(),
            event.getPermanent()
        ));
    }
};

def GainTrig = new MagicStatic(MagicLayer.Ability) {
    @Override
    public void modAbilityFlags(final MagicPermanent source, final MagicPermanent permanent, final Set<MagicAbility> flags) {
        permanent.addAbility(Duplicate);
    }
};

[
    new MagicETBEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                new MagicMayChoice(A_CREATURE),
                MagicCopyPermanentPicker.create(),
                this,
                "PN may\$ have SN enter the battlefield as a copy of any creature\$ on the battlefield, " +
                "except it gains \"At the beginning of your upkeep, if this creature isn't a token, create a token that's a copy of this creature.\""
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new EnterAsCopyAction(event.getCardOnStack(), event.getTarget(), {
                    final MagicPermanent perm ->
                    final MagicGame G = perm.getGame();
                    G.doAction(new AddStaticAction(perm, GainTrig));
                }));
            } else {
                game.doAction(new PlayCardFromStackAction(
                    event.getCardOnStack()
                ));
            }
        }
    }
]
