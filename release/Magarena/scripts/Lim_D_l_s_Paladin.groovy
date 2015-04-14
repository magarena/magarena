[
    new MagicAtYourUpkeepTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer upkeepPlayer) {
            return new MagicEvent(
                permanent,
                new MagicMayChoice("Discard a card?"),
                this,
                "PN may\$ discard a card. If PN doesn't, sacrifice SN and draw a card."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicEvent discard = new MagicDiscardEvent(event.getSource(), event.getPlayer());
            if (event.isYes() && discard.isSatisfied()) {
                game.addEvent(discard);
            } else {
                game.doAction(new MagicSacrificeAction(event.getPermanent()));
                game.doAction(new DrawAction(event.getPlayer()));
            }
        }
    },
    new MagicWhenAttacksUnblockedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
            return (creature == permanent) ?
                new MagicEvent(
                    permanent,
                    this,
                    "SN assigns no combat damage this turn and defending player loses 4 life."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(ChangeStateAction.Set(
                event.getPermanent(),
                MagicPermanentState.NoCombatDamage
            ));
            game.doAction(new ChangeLifeAction(game.getDefendingPlayer(), -4));
        }
    }
]
