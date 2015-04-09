[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return upkeepPlayer.getOpponent().getNrOfPermanents(MagicType.Creature) >= 3 ?
                new MagicEvent(
                    permanent,
                    this,
                    "Sacrifice SN. PN searches his or her library for up to two creature cards and puts them onto the battlefield. "+
                    "Then PN shuffles his or her library."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.addEvent(new MagicSacrificeEvent(event.getPermanent()));
            game.addEvent(new MagicSearchOntoBattlefieldEvent(
                event,
                new MagicFromCardFilterChoice(
                    CREATURE_CARD_FROM_LIBRARY,
                    2, 
                    true, 
                    "to put onto the battlefield"
                )
            ));
        }
    }
]
