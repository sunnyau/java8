package thread;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import model.*;
import static org.junit.Assert.assertTrue;

/**
 * This test get 
 * 
 * @author Sunny
 * 
 *
 */
public class CompletableFutureTest {

    @Before	
	public void setUp() throws Exception {
	}


    /**
     * Single thread example.
     * 
     * We get the price from each online shop.
     */
	@Test
	public void getPriceFromShopOneByOne() throws InterruptedException {
		
		LocalDateTime startDt = LocalDateTime.now();
		Shop onlineShop = new Shop();
		
		double totalPrice = 0;
		totalPrice += onlineShop.getPrice("book");
		totalPrice += onlineShop.getPrice("phone");
		totalPrice += onlineShop.getPrice("battery");
		totalPrice += onlineShop.getPrice("pen");
		
		long msTaken = startDt.until( LocalDateTime.now(), ChronoUnit.MILLIS);
		System.out.println("Time taken [" + msTaken + "] ms. total price = [�" + String.format( "%.2f", totalPrice ) + "]" );
		assertTrue(true);
	}
	
	/**
	 * Although thread was introduced since Java was born, it was quite difficult to use.
	 * 
	 * In JDK 5. Future, ExecutorService and Callable interface are introduced to make run multiple threads easier.
	 * 
	 * Any class implementing Callable interface is a thread.
	 * ExecutorService is a thread pool.
	 * Future interface holds a value, which you can get in the future.
	 */
	@Test
	public void getPriceFromShopsAtTheSameTime() throws InterruptedException, ExecutionException {
		
		LocalDateTime startDt = LocalDateTime.now();
		Shop onlineShop = new Shop();
		
		// thread pool with 4 threads.
		ExecutorService executorService = Executors.newFixedThreadPool(4);
		
		Future<Double> future1 = executorService.submit( new CallableTask( onlineShop, "book" ) );
		Future<Double> future2 = executorService.submit( new CallableTask( onlineShop, "phone" ) );
		Future<Double> future3 = executorService.submit( new CallableTask( onlineShop, "battery" ) );		
		Future<Double> future4 = executorService.submit( new CallableTask( onlineShop,"pen" ) );
		
		// no more submit.
		executorService.shutdown();
		
		double totalPrice = 0;
		totalPrice = future1.get();
		totalPrice += future2.get();
		totalPrice += future3.get();
		totalPrice += future4.get();		
		
		long msTaken = startDt.until( LocalDateTime.now(), ChronoUnit.MILLIS);
		System.out.println("Time taken [" + msTaken + "] ms. total price = [�" + String.format( "%.2f", totalPrice ) + "]" );
		assertTrue(true);
	}
	
	/**
	 * CompletableFuture
	 * 
	 * Instead of creating an ExecutorService and passing in Callable, you can just use CompletableFuture.supplyAsync() instead.
	 * Note : You need to change the jre to 1.8
	 */
	@Test
	public void getPriceFromShopsUsingSupplyAsync() throws InterruptedException, ExecutionException {
		
		LocalDateTime startDt = LocalDateTime.now();
		Shop onlineShop = new Shop();
		 
		Future<Double> future1 = CompletableFuture.supplyAsync(() -> onlineShop.getPrice("book") );
		Future<Double> future2 = CompletableFuture.supplyAsync(() -> onlineShop.getPrice("phone"));
 		Future<Double> future3 = CompletableFuture.supplyAsync(() -> onlineShop.getPrice("battery"));		
		Future<Double> future4 = CompletableFuture.supplyAsync(() -> onlineShop.getPrice("pen"));
		
		
		double totalPrice = 0;
		totalPrice = future1.get();
		totalPrice += future2.get();
		totalPrice += future3.get();
		totalPrice += future4.get();		
		
		long msTaken = startDt.until( LocalDateTime.now(), ChronoUnit.MILLIS);
		System.out.println("Time taken [" + msTaken + "] ms. total price = [�" + String.format( "%.2f", totalPrice ) + "]" );
		assertTrue(true);
	}
	
	
	
}
