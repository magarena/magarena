[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicCardOnStack spell) {
            return (spell.isFriend(permanent) && spell.isSpell(MagicType.Enchantment)) ?
                new MagicEvent(
                    permanent,
                    this,
                    "PN put a 4/4 white Angel creature token with flying onto the battlefield."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("4/4 white Angel creature token with flying")));
        }
    }
]
