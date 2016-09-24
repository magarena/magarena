def choice = new MagicTargetChoice("a Spirit permanent card from your library");

def SPIRIT_PERMANENT_FROM_GRAVEYARD = new MagicCardFilterImpl() {
    public boolean accept(final MagicSource source,final MagicPlayer player,final MagicCard target) {
        return target.hasSubType(MagicSubType.Spirit) && target.isPermanentCard();
    }
    public boolean acceptType(final MagicTargetType targetType) {
        return targetType == MagicTargetType.Graveyard;
    }
};

def TARGET_SPIRIT_PERMANENT_FROM_GRAVEYARD = new MagicTargetChoice(
    SPIRIT_PERMANENT_FROM_GRAVEYARD,
    MagicTargetHint.None,
    "target Spirit permanent card from your graveyard"
);

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return permanent.hasState(MagicPermanentState.CastFromHand) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ search his or her library for a Spirit permanent card, " +
                    "put it onto the battlefield, then shuffle his or her library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(new MagicSearchOntoBattlefieldEvent(
                    event.getSource(),
                    event.getPlayer(),
                    choice
                ));
            }
        }
    },
    new ThisDiesTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent died) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice(TARGET_SPIRIT_PERMANENT_FROM_GRAVEYARD),
                this,
                "PN may\$ exile SN. If PN does, return target Spirit permanent card\$ from his or her graveyard to the battlefield."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()){
                event.processTargetCard(game, {
                    final MagicCard card = event.getPermanent().getCard();
                    if (card.isInGraveyard()) {
                        game.doAction(new ShiftCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
                        game.doAction(new ReanimateAction(
                            it,
                            event.getPlayer()
                        ));
                    }
                });
            }
        }
    }
]
