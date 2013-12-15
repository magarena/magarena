[
    new MagicWhenOtherSpellIsCastTrigger() {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicCardOnStack spell) {
            return spell.hasSubType(MagicSubType.Elf);
        }
        @Override
        public MagicEvent executeTrigger(final MagicGame game, final MagicPermanent permanent, final MagicCardOnStack cardOnStack) {
            new MagicEvent(
				permanent,
				new MagicMayChoice(),
				this,
				"PN may\$ put a 1/1 green Elf Warrior creature token onto the battlefield."
			);
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            if (event.isYes()) {
                game.doAction(new MagicPlayTokenAction(
                    event.getPlayer(), 
                    TokenCardDefinitions.get("1/1 green Elf Warrior creature token")
                ));
            }
        }
    }
]
