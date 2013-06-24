[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (permanent.isFriend(spell) &&
                    spell.getCardDefinition().isArtifact()) ?
                new MagicEvent(
                    permanent,
                    new MagicMayChoice(
                        new MagicPayManaCostChoice(MagicManaCost.create("{1}"))
                    ),
                    this,
                    "PN may\$ pay {1}\$. If you do, put a 1/1 " +
                    "colorless Myr artifact creature token onto the battlefield."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(event.getPlayer(),TokenCardDefinitions.get("Myr1")));
            }
        }
    }
]
