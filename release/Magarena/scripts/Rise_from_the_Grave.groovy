[
    new MagicSpellCardEvent() {
        @Override
        public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
            return new MagicEvent(
                cardOnStack,
                MagicTargetChoice.TARGET_CREATURE_CARD_FROM_ALL_GRAVEYARDS,
                new MagicGraveyardTargetPicker(true),
                this,
                "Put target creature\$ card from a graveyard onto the battlefield under your control. " +
                "That creature is a black Zombie in addition to its other colors and types."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            event.processTargetCard(game, {
                final MagicCard card ->

                final MagicReanimateAction action = new MagicReanimateAction(
                    card,
                    event.getPlayer(),
                    MagicPlayCardAction.NONE
                );
                game.doAction(action);

                final MagicPermanent permanent = action.getPermanent();
                if (permanent.isValid()) {
                    game.doAction(new MagicAddStaticAction(permanent, MagicStatic.Zombie));
                    game.doAction(new MagicAddStaticAction(permanent, MagicStatic.Black));
                }
            } as MagicCardAction);
        }
    }
]
