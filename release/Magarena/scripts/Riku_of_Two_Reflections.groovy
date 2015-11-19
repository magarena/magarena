[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            return (permanent.isFriend(cardOnStack) && cardOnStack.isInstantOrSorcerySpell()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{U}{R}"))
                    ),
                    cardOnStack,
                    this,
                    "PN may\$ pay {U}{R}. If PN does, copy RN. PN may choose new targets for the copy."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new CopyCardOnStackAction(event.getPlayer(),event.getRefCardOnStack()));
            }
        }
    },
    new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicPermanent otherPermanent) {
            return (otherPermanent != permanent &&
                    otherPermanent.isNonToken() &&
                    otherPermanent.isCreature() &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{G}{U}"))
                    ),
                    otherPermanent,
                    this,
                    "PN may\$ pay {G}{U}. If PN does, he or she puts a token that's a copy of RN onto the battlefield."
                ):
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new PlayTokenAction(
                    event.getPlayer(),
                    event.getRefPermanent()
                ));
            }
        }
    }
]
