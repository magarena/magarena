def choice = new MagicTargetChoice("target creature card from your graveyard");

[
    new AtEndOfTurnTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPlayer eotPlayer) {
            return permanent.isController(eotPlayer) ?
                new MagicEvent(
                permanent,
                choice,
                this,
                "PN chooses target creature card from his or her graveyard\$. If that card's converted mana cost is less than or equal to the number of experience counters PN have, return it to the battlefield. "+
                "Otherwise, PN puts that card into his or her hand."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicPlayer player = event.getPlayer();
                final int XP = player.getExperience();
                if (it.getConvertedCost() <= XP) {
                    game.doAction(new ReanimateAction(it,player));
                } else {
                    game.doAction(new ShiftCardAction(it,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                    game.logAppendMessage(player, "${player.getName()} returns (${it.getName()}) to his or her hand.");
                }
            });
        }
    }
]
