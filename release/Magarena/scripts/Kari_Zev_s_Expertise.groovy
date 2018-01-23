def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        event.processTargetCard(game, {
            game.doAction(CastCardAction.WithoutManaCost(event.getPlayer(), it, MagicLocationType.OwnersHand, MagicLocationType.Graveyard));
        });
    }
}

[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.Negative("target creature or Vehicle"),
                this,
                "Gain control of target creature of Vehicle\$ until end of turn. " +
                "Untap it. It gains haste until end of turn."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {

            event.processTargetPermanent(game, {
                game.doAction(new GainControlAction(event.getPlayer(), it, MagicStatic.UntilEOT));
                game.doAction(new UntapAction(it));
                game.doAction(new GainAbilityAction(it, MagicAbility.Haste, MagicStatic.UntilEOT));
            });

            game.addEvent(new MagicEvent(
                event.getSource(),
                new MagicMayChoice(new MagicTargetChoice(
                    card().cmcLEQ(2).from(MagicTargetType.Hand),
                    "a card with converted mana cost 2 or less from your hand"
                )),
                action,
                "PN may\$ cast a card with converted mana cost 2 or less from PN's hand\$ without paying its mana cost."
            ));
        }
    }
]

