/**
 * @author STomney
 * @version 1.0
 */
package au.com.latitude.microservice.lambda.MaxDailyStockProfit;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.logging.*;

public final class MaxDailyStockProfit {

	/**
	 * This class will accept an array of double format numbers for share prices. 
	 * It will determine the best profit that can be obtained provided you sell before you buy.
	 * The class also ensures that the highest number must be before the lowest number. ie. Buy before selling your share.
	 * The indices in the array are the time in minutes past trade opening time.
	 * 
	 * The request is expecting:
	 *  1. The array of double formated share prices - Array Double[]
	 *  2. stock exchange opening time - String
	 * 
	 * The response returns an array of:
	 *  - maximumStockPriceValue
	 *  - finalMaximumPriceIndex
	 *  - minimumStockPriceValue
	 *  - finalMinimumPriceIndex
	 *  - bestProfit (NOTE: zero will be returned if there is not profit to be made.)
	 *  
	 *  The data in the array is then used to output the:
	 *  - BEST PROFIT
	 *  - TIME TO SELL eg. 10:01
	 *  - TIME TO BUY  eg. 10:05
	 *  
	 *  (NOTE: Stock exchange opens at 10am ) 
	 */
	private static final Logger LOGGER = Logger.getLogger( MaxDailyStockProfit.class.getName() );
	
	public MaxDailyStockProfit(double[] stock_prices_yesterday, String stock_exchange_opening_time, String stock_exchange_closing_time) {
		
		double[] BestStockPriceProfit;  //highest stock price for input array 
		int minutesOpen;
		//* error handling check the length of the array is less than 361
		try {			
			
			// determine total number of indexes in array
			int arrayLength;
			
			//min share price must not be the last entry in the array as stock must be bought before you can sell
			arrayLength = stock_prices_yesterday.length;
			
			LocalTime stockExchangeOpens = LocalTime.parse(stock_exchange_opening_time);
			
			minutesOpen = determineMinutesOpen(stock_exchange_opening_time, stock_exchange_closing_time);
			
			// check whether the input array is too long for the number of minutes the exchange is open for. If yes, throw an exception
			if ( arrayLength > minutesOpen )
			{
				throw new Exception("Input array data is too long given the stock exchange closing time. Input array length: " + arrayLength + ". Number of minutes exchange open for: " + minutesOpen);
			}
				
			BestStockPriceProfit = bestProfit(stock_prices_yesterday, arrayLength);

			System.out.println( "TOTAL PROFIT IS: ");
			NumberFormat formatter = new DecimalFormat("$#0.000");  
			System.out.println(formatter.format(BestStockPriceProfit[4])); //output the profit correct to 3 decimal places

			if ( BestStockPriceProfit[4] == 0.000 )
			{
				System.out.println( "TIME TO BUY: NOT APPLICABLE - STOCK MAKRET WENT DOWN ALL DAY OR THE PRICE DID NOT CHANGE");
				System.out.println( "TIME TO SELL: NOT APPLICABLE - STOCK MARKET WENT DOWN ALL DAY OR THE PRICE DID NOT CHANGE");
			} else {
				LocalTime buyTime = stockExchangeOpens.plus(Duration.ofMinutes((int)BestStockPriceProfit[3]) );
				LocalTime sellTime = stockExchangeOpens.plus(Duration.ofMinutes((int)BestStockPriceProfit[1]) );
				
				
				System.out.println( "TIME TO BUY: " + buyTime + " BUY AMOUNT: " + BestStockPriceProfit[2]);
				System.out.println( "TIME TO SELL: " + sellTime + " SELL AMOUNT: " + BestStockPriceProfit[0]);
			}
		}
		catch (Exception e)
        {
        	LOGGER.log(Level.SEVERE, e.toString(), e );	
        	System.out.println( "Exception caught. Error occurred. Refer to exception for more details.");
        }
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		//double[] stock_prices_yesterday =  {400.001, 1500.002, 9200.00, 14000, 23000.003}; //Test: stock price goes up all day. Expected result: $22600.002
		double[] stock_prices_yesterday =  {40000.001, 1500.002, 200.00, 14, 3.003}; //Test: stock price goes down all day. Expected result: $0.000
		//double[] stock_prices_yesterday =  {400.001, 1500.002, 119200.00, 14000, 23000.003}; //Test: highest stock price is in the middle of the day. Expected result: $118799.999
		//double[] stock_prices_yesterday =  {400.001, 400.001, 400.001, 400.001, 400.001}; //Test: stock market price did not change all day. Expected result: $0.000
		//double[] stock_prices_yesterday =  {0.023, 1500.002, 0.023}; //Expected result: $1499.979
		//double[] stock_prices_yesterday =  {4400.001, 150.002, 9200.003, 4000.000, 23000.003}; //Expected result: $22850.001
		//double[] stock_prices_yesterday =  {400.001, 1500.002, 1.500, 20.000, 1.003}; //Test: lowest price is last index. Expected result: $1100.001
		//double[] stock_prices_yesterday =  {5.0, 2.0, 1.0, 4.000, 0.500}; //Test: highest stock price is 1st entry. Expected result: $3.000
		
		try {
			MaxDailyStockProfit bestProfit = new MaxDailyStockProfit(stock_prices_yesterday, "10:00", "11:02");
			System.out.println( "Transaction Successfully complete: " + bestProfit);
		}
		catch (Exception e)
        {
        	LOGGER.log(Level.SEVERE, e.toString(), e );	
        	System.out.println( "Exception caught. Error occurred. Refer to exception for more details.");
        }

	}

	public static int determineMinutesOpen (String stock_exchange_opening_time, String stock_exchange_closing_time )
	{
		
		// determine the number of minutes the stock exchange is open for
		LocalTime stockExchangeOpens = LocalTime.parse(stock_exchange_opening_time);
		LocalTime stockExchangeCloses = LocalTime.parse(stock_exchange_closing_time);
		long minutes = ChronoUnit.MINUTES.between(stockExchangeOpens, stockExchangeCloses);
		LOGGER.info( "The number of minutes the stock exchange is open for: " + minutes );
		
		return ((int)minutes);
	}
	
	public static double bestProfitTest ( double[] stock_prices_yesterday, int arrayLength)
	{
		double[] BestStockPriceProfit; 
		BestStockPriceProfit = bestProfit(stock_prices_yesterday, arrayLength);
		return (BestStockPriceProfit[4]); //returns best stock price profit found in input array
	}
	
	public static double[] bestProfit ( double[] stock_prices_yesterday, int arrayLength)
	{
		// declare variables and initalise
		int arrayPriceIndex = 0; // array index
		int finalMinimumPriceIndex = 0; // final minimum price index initalise
		int finalMaximumPriceIndex = 0; // final maximum price index initalise
		double maximumStockPriceValue = stock_prices_yesterday[1]; //initial highest value - can't be first entry in array as you must before you sell
		double minimumStockPriceValue = stock_prices_yesterday[0]; 
		double[] responseMinAndMaxPriceAndIndex = new double[5]; // array to return results in
		double bestProfit = 0;
			
		System.out.println( "stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " maximumStockPriceValue: " + maximumStockPriceValue );
		System.out.println( "stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " minimumStockPriceValue: " + minimumStockPriceValue );
		
		// loop through array of stock prices. Each index is the stock price for the minute.
		for ( arrayPriceIndex = 1 ; arrayPriceIndex < arrayLength ; arrayPriceIndex ++ )
	   	 {
			System.out.println( "OUTER stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " maximumStockPriceValue: " + maximumStockPriceValue );
			
	   	    if ( stock_prices_yesterday[arrayPriceIndex ] > maximumStockPriceValue)
	   	    {
	   	    	System.out.println( "INNER stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " maximumStockPriceValue: " + maximumStockPriceValue );
				
	   	    	maximumStockPriceValue = stock_prices_yesterday[arrayPriceIndex ];  // Found a larger max. value
	   	    	finalMaximumPriceIndex = arrayPriceIndex; // record index number for max. value
	   	    	LOGGER.info( "Max stock price value: " + maximumStockPriceValue + " Max stock price index: " + finalMaximumPriceIndex);
	   	    }
	   	 }
		for ( arrayPriceIndex = 1 ; arrayPriceIndex < finalMaximumPriceIndex ; arrayPriceIndex ++ )
	   	 {
			System.out.println( "OUTER stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " minimumStockPriceValue: " + maximumStockPriceValue );
			
	    	if ( stock_prices_yesterday[arrayPriceIndex] < minimumStockPriceValue )
	    	{
	    		System.out.println( "INNER stock price: " + stock_prices_yesterday[arrayPriceIndex ] + " minimumStockPriceValue: " + maximumStockPriceValue );
				
	    		minimumStockPriceValue = stock_prices_yesterday[arrayPriceIndex ];  // Found a smaller min. value
	    		finalMinimumPriceIndex = arrayPriceIndex; // record index number for min. value	
	    		LOGGER.info( "Min stock price value: " + minimumStockPriceValue + " Min stock price index: " + finalMinimumPriceIndex);
	    	} 
	   	 }
		LOGGER.info( "Determining best profit... " );
    	bestProfit = maximumStockPriceValue - minimumStockPriceValue;
    	if ( bestProfit < 0 )
    	{
    		bestProfit = 0; //If the stock price goes down all day then the bestProfit could end up being a negative number
    	}
    	LOGGER.info( "BEST PROFIT IS: " + bestProfit);
    	
		LOGGER.severe( "MaximumIndex: " + finalMaximumPriceIndex );
		LOGGER.severe( "MinimumIndex: " + finalMinimumPriceIndex );

		// write highest stock price and index, plus lowest stock price and index, plus bestProfit to responseArray
		responseMinAndMaxPriceAndIndex[0] = maximumStockPriceValue;
	   	responseMinAndMaxPriceAndIndex[1] = finalMaximumPriceIndex;
	   	responseMinAndMaxPriceAndIndex[2] = minimumStockPriceValue;
	   	responseMinAndMaxPriceAndIndex[3] = finalMinimumPriceIndex;   	
	   	responseMinAndMaxPriceAndIndex[4] = bestProfit;
	   	
	   	//check if bestProfit is zero
	   	if ( (int)bestProfit == 0 )
	   	{
	   		LOGGER.severe( "No profit to be made as the stock price went down all day or the price did not change: " + bestProfit );
	  
	   	} else {		
	   		
	   		LOGGER.severe( "Best profit to be made is: " + bestProfit );
	   	}

		return (responseMinAndMaxPriceAndIndex); //returns best stock price profit found in input array
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return LOGGER;
	}
	
	

}
