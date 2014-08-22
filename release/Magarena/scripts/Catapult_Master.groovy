def UNTAPPED_SOLDIER_YOU_CONTROL=new MagicPermanentFilterImpl(){
public boolean accept(final MagicGame game,final MagicPlayer player,final MagicPermanent target)
{
return target.hasSubType(MagicSubType.SOLDIER) &&
target.isUntapped() &&
target.isController(player);
}
};

def FIVE_UNTAPPED_SOLDIER_CONDITION = new MagicCondition() {
public boolean accept(final MagicSource source) {
return source.getController().getNrOfPermanents(UNTAPPED_SOLDIER_YOU_CONTROL) >= 5;
}
};

def AN_UNTAPPED_SOLDIER_YOU_CONTROL = new MagicTargetChoice(UNTAPPED_SOLDIER_YOU_CONTROL,"an untapped SOLDIER you control");

[
new MagicPermanentActivation(
[FIVE_UNTAPPED_SOLDIER_CONDITION],
new MagicActivationHints(MagicTiming.Removal),
"Untap"
) {
	@Override
	public Iterable<MagicEvent> getCostEvent(final MagicPermanent source) {
		return [
			new MagicTapPermanentEvent(source, AN_UNTAPPED_SOLDIER_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_SOLDIER_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_SOLDIER_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_SOLDIER_YOU_CONTROL),
			new MagicTapPermanentEvent(source, AN_UNTAPPED_SOLDIER_YOU_CONTROL)
		];
	}

	@Override
	public MagicEvent getPermanentEvent(final MagicPermanent source, final MagicPayedCost payedCost) {		
		return new MagicEvent(
			source,
			MagicTargetChoice.NEG_TARGET_CREATURE,
			MagicExileTargetPicker.create(),
			this,
			"Exile target creature\$."
		);		
	}

	@Override
	public void executeEvent(final MagicGame game, final MagicEvent event) {
		final MagicPermanent creature ->
		game.doAction(new MagicRemoveFromPlayAction(creature,MagicLocationType.Exile));
	}
}
]