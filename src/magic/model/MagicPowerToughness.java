package magic.model;

public class MagicPowerToughness {

	private int power;
	private int toughness;
	
	MagicPowerToughness(final int power,final int toughness) {
		this.power=power;
		this.toughness=toughness;
	}

    public int power() {
        return power;
    }

    public int toughness() {
        return toughness;
    }
	
	public int getPositivePower() {
		return power>0?power:0;
	}
	
	public int getPositiveToughness() {
		return toughness>0?toughness:0;
	}
	
    public void add(final int pAmount, final int tAmount) {
		power     += pAmount;
		toughness += tAmount;
	}
    
    public void set(final int pAmount, final int tAmount) {
		power     = pAmount;
		toughness = tAmount;
	}
}
