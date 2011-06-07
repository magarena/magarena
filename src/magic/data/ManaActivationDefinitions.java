package magic.data;

import magic.model.MagicCardDefinition;
import magic.model.MagicColor;
import magic.model.MagicManaType;
import magic.model.event.MagicManaActivation;
import magic.model.event.MagicSacrificeTapManaActivation;
import magic.model.event.MagicTapManaActivation;
import magic.model.event.MagicVividManaActivation;

public class ManaActivationDefinitions {
	
	private final static void addManaActivation(final String name,final MagicManaActivation manaActivation) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(manaActivation);
	}
	
	private final static void addCreatureActivations(final String name,final MagicManaType manaTypes[]) {

		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(manaTypes,1));
		cardDefinition.setExcludeManaOrCombat();
	}
		
	private final static void addVividActivations(final String name,final MagicManaType manaType) {
	
		final MagicManaType manaTypes[]=new MagicManaType[MagicColor.NR_COLORS-1];
		int count=0;		
		for (final MagicColor color : MagicColor.values()) {
			
			final MagicManaType colorManaType=color.getManaType();
			if (colorManaType!=manaType) {
				manaTypes[count++]=colorManaType;
			}
		}
		
		final MagicCardDefinition cardDefinition=CardDefinitions.getInstance().getCard(name);
		cardDefinition.addManaActivation(new MagicTapManaActivation(new MagicManaType[]{MagicManaType.Colorless,manaType},0));
		cardDefinition.addManaActivation(new MagicVividManaActivation(manaTypes));
	}
	
	public static void addManaActivations() {
		// Vivid lands.
		addVividActivations("Vivid Crag",MagicManaType.Red);
		addVividActivations("Vivid Creek",MagicManaType.Blue);
		addVividActivations("Vivid Grove",MagicManaType.Green);
		addVividActivations("Vivid Marsh",MagicManaType.Black);
		addVividActivations("Vivid Meadow",MagicManaType.White);
		
		// Artifacts.
		addManaActivation("Lotus Petal",new MagicSacrificeTapManaActivation(MagicManaType.ALL_TYPES));
		addManaActivation("Mind Stone",new MagicTapManaActivation(new MagicManaType[]{MagicManaType.Colorless},0));

        // Land generating colorless mana
        addManaActivation("Tectonic Edge",new MagicTapManaActivation(new MagicManaType[]{MagicManaType.Colorless},0));
		
		// Man lands.
		addCreatureActivations("Celestial Colonnade",new MagicManaType[]{MagicManaType.Blue,MagicManaType.White});
		addCreatureActivations("Creeping Tar Pit",new MagicManaType[]{MagicManaType.Blue,MagicManaType.Black});
		addCreatureActivations("Raging Ravine",new MagicManaType[]{MagicManaType.Red,MagicManaType.Green});
		addCreatureActivations("Stirring Wildwood",new MagicManaType[]{MagicManaType.Green,MagicManaType.White});		
		addCreatureActivations("Inkmoth Nexus",new MagicManaType[]{MagicManaType.Colorless});		

		// Creatures.
		addCreatureActivations("Alloy Myr",MagicManaType.ALL_TYPES);
		addCreatureActivations("Birds of Paradise",MagicManaType.ALL_TYPES);
		addCreatureActivations("Llanowar Elves",new MagicManaType[]{MagicManaType.Colorless,MagicManaType.Green});
		addCreatureActivations("Noble Hierarch",new MagicManaType[]{MagicManaType.Colorless,MagicManaType.Blue,MagicManaType.Green,MagicManaType.White});
		addCreatureActivations("Steward of Valeron",new MagicManaType[]{MagicManaType.Colorless,MagicManaType.Green});
		addCreatureActivations("Vine Trellis",new MagicManaType[]{MagicManaType.Colorless,MagicManaType.Green});
		addCreatureActivations("Plague Myr",new MagicManaType[]{MagicManaType.Colorless});
		
        System.err.println("Added 21 mana activations");
	}
}
