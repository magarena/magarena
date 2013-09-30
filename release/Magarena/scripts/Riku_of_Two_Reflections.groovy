[
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.getController() == permanent.getController()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{U}{G}"))
                    ),
					otherPermanent,
                    this,
                    "You may\$ pay {U}{G}\$. If you do, put a creature token onto the battlefield that's a copy of RN."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(),
                    event.getRefPermanent().getCardDefinition()
                ));
            }
        }
    },
	new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicCardOnStack cardOnStack) {
            return (permanent.isController(cardOnStack.getController()) &&
                    cardOnStack.getCardDefinition().isSpell()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{U}{R}"))
                    ),
					cardOnStack,
                    this,
                    "You may\$ pay {U}{R}\$. If you do, copy RN. You may choose new targets for the copy."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
			if (event.isYes()) {
                game.doAction(new MagicCopyCardOnStackAction(event.getPlayer(),event.getRefCardOnStack()));
            }
           
        }
    }
]