package edu.ruc.IOS;

public class LiteralBinding {

	private int id; //can be positive or negative. Equal to variable id * 1 or -1
	private int value;// -1 , 1 , 0 NotFixed
	

	
	public LiteralBinding(int id) {
		super();
		this.id = id;
		this.value = 0;
	}


	public LiteralBinding(int id, int value) {
		super();
		this.id = id;
		this.value = value;
	}
	
	public String toString(){
		return this.id + "|" + this.value;		
	}


	/*getters and setters */
	public int getId() {
		return id;
	}

 
	public int getValue() {
		return value;
	}


	public void setvalue(int value) {
		this.value = value;
	}


	 

}
