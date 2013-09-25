[    
	new MagicPermanentActivation(
        new MagicActivationHints(MagicTiming.Token),                                                                                                                                        
        "Kaldra"                                                                                                                                                                               
    ) { 
		@Override
        public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
            return [new MagicPayManaCostEvent(source,"{1}")];
        }
		@Override
        public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
            final MagicPlayer player = source.getController();
			final Collection<MagicPermanent> targets =
                    player.getGame().filterPermanents(player,MagicTargetFilter.TARGET_EQUIPMENT);
			boolean sword = false;
			boolean shield = false;
			boolean helm = false;
			for (final MagicPermanent equip : targets) {
				if (equip.getName() == "Sword of Kaldra"){ sword = true;} 
				if (equip.getName() == "Shield of Kaldra"){ shield = true;} 
				if (equip.getName() == "Helm of Kaldra"){ helm = true;} 
            }
			
			return (sword && shield && helm)? 
				new MagicEvent(
					source,
					this,
					"PN puts a legendary 4/4 colorless Avatar creature token named Kaldra onto the battlefield."
                ) :
				MagicEvent.NONE;
        }
		@Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicPlayTokenAction(event.getPlayer(), TokenCardDefinitions.get("Kaldra")));
        }
	},
	new MagicWhenOtherComesIntoPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            return (otherPermanent.isCreature() &&
					otherPermanent.getName() == "Kaldra" &&
                    otherPermanent.isFriend(permanent)) ?
                new MagicEvent(
                    permanent,
                    otherPermanent,
                    this,
                    "Attach SN to RN."
                ) :
                MagicEvent.NONE;
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicAttachAction(
                event.getPermanent(),
                event.getRefPermanent()
            ));
        }
    }
]