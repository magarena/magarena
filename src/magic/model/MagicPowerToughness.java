package magic.model;

public class MagicPowerToughness {

	public int power;
	public int toughness;
	
	MagicPowerToughness(final int power,final int toughness) {
		this.power=power;
		this.toughness=toughness;
	}
	
	private MagicPowerToughness() {
		this(0,0);
	}
	
	public int getPositivePower() {
		return power>0?power:0;
	}
	
	public int getPositiveToughness() {
		return toughness>0?toughness:0;
	}
	
	void add(final int amount) {
		power+=amount;
		toughness+=amount;
	}
}
