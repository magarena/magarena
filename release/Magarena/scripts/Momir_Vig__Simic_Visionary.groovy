[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isFriend(cardOnStack) &&
                   cardOnStack.hasType(MagicType.Creature) &&
                   cardOnStack.hasColor(MagicColor.Green) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(),
                    this,
                    "PN may\$ search your library for a creature card and reveal it. "+
                    "If you do, shuffle your library and put that card on top of it."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.addEvent(
                MagicRuleEventAction.create(
                "Search your library for a creature card, reveal it, then shuffle your library and put that card on top of it.").getEvent(event.getSource()
                ));
            }
        }
    },
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack cardOnStack) {
            return permanent.isFriend(cardOnStack) &&
                   cardOnStack.hasType(MagicType.Creature) &&
                   cardOnStack.hasColor(MagicColor.Blue) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Reveal the top card of PN's library. If it's a creature card, put that card into PN's hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            for (final MagicCard card : event.getPlayer().getLibrary().getCardsFromTop(1)) {
                game.doAction(new RevealAction(card));
       
                game.doAction(new RemoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary
                ));
                game.doAction(new MoveCardAction(
                    card,
                    MagicLocationType.OwnersLibrary,
                    card.hasType(MagicType.Creature) ?
                      MagicLocationType.OwnersHand :
                      MagicLocationType.OwnersLibrary
                ));       
            }      
        }
    }
]
