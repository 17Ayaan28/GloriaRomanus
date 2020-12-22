package unsw.gloriaromanus.buildings;



public interface Building {

    public void upgrade();
    public int getBaseWealth();
	public boolean isReady();
    public int getCost();
	public int getTurnsLeft();

}
