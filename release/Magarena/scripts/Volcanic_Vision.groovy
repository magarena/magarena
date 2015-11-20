def choice = new MagicTargetChoice("target instant or sorcery card from your graveyard");

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                choice,
                this,
                "PN returns target instant or sorcery card from his or her graveyard to his or her hand.\$ "+
                "SN deals damage equal to that card's converted mana cost to each creature PN's opponents control. "+
                "Exile SN."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final int amount = it.getConvertedCost();
                game.logAppendValue(event.getPlayer(),amount);
                game.doAction(new ShiftCardAction(it, MagicLocationType.Graveyard, MagicLocationType.OwnersHand));
                CREATURE_YOUR_OPPONENT_CONTROLS.filter(event) each {
                    game.doAction(new DealDamageAction(event.getSource(), it, amount));
                }
                game.doAction(new ChangeCardDestinationAction(event.getCardOnStack(), MagicLocationType.Exile));
            });
        }
    }
]
