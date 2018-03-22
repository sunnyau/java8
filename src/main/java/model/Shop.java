package model;

import java.util.Random;


/**
 * 
 * @author User
 *
 */
public class Shop {

	private Random random = new Random();
	
	/**
	 * Wait for 1 second.
	 */
	public double getPrice(String product)  {
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}


}
