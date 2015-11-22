def choice = new MagicTargetChoice("target artifact card from your graveyard");

[
    new EntersBattlefieldTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPayedCost payedCost) {
            return new MagicEvent(
                permanent,
                choice,
                this,
                "PN returns target artifact card from his or her graveyard to his or her hand.\$ "+
                "PN gains life equal to that card's converted mana cost."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final int amount = it.getConvertedCost();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
                game.doAction(new ChangeLifeAction(event.getPlayer(), amount));
            });
        }
    }
]
