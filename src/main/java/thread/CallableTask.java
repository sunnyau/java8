package thread;

import java.util.concurrent.Callable;
import model.Shop;

/**
 * This task is run by ExecutorService ( i.e thread pool )
 * @author Sunny
 */
public class CallableTask implements Callable<Double> {

	private Shop shop;
	private String product;
	
	public CallableTask( Shop shop_, String product_ ) {
		super();
		this.shop = shop_;
		this.product = product_;
	}

	@Override
	public Double call() throws Exception {
		return shop.getPrice(product);	    
	}

}
