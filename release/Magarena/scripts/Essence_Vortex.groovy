def action = {
    final MagicGame game, final MagicEvent event ->
    if (event.isYes()) {
        final int toughness = event.getRefPermanent().getToughness()
        game.addEvent(new MagicPayLifeEvent(event.getSource(), event.getPlayer(), toughness));
    } else {
        game.doAction(MagicChangeStateAction.Set(event.getRefPermanent(), MagicPermanentState.CannotBeRegenerated));
        game.doAction(new MagicDestroyAction(event.getRefPermanent()));
    }
}

[
    new MagicSpellCardEvent() {

        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack, final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.NEG_TARGET_CREATURE,
                MagicDestroyTargetPicker.DestroyNoRegen,
                this,
                "Destroy target creature\$ unless its controller pays life equal to its toughness."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetPermanent(game, {
                final MagicPermanent permanent ->
                game.addEvent(new MagicEvent(
                    event.getSource(),
                    permanent.getController(),
                    new MagicMayChoice("Pay "+permanent.getToughness()+" life?"),
                    permanent,
                    action,
                    "PN may\$ pay life equal to ("+permanent.toString()+")'s toughness."
                ));
            });
        }
    }
]
