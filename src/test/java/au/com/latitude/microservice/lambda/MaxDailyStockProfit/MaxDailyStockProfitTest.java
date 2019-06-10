package au.com.latitude.microservice.lambda.MaxDailyStockProfit;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class MaxDailyStockProfitTest 
    extends TestCase
{
		
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public MaxDailyStockProfitTest( String test9_determineMinutesOpen )
    {
        super( test9_determineMinutesOpen ); 

    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( MaxDailyStockProfitTest.class );
    }

    public void test1_stockPriceGoesUp()
    {
    	double[] stock_prices_yesterday = {400.001, 1500.002, 9200.00, 14000, 23000.003}; //Test: stock price goes up all day. Expected result: $22600.002
    	assertEquals(22600.002, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }
    
    public void test2_stockPriceGoesDown()
    {
    	double[] stock_prices_yesterday =  {40000.001, 1500.002, 200.00, 14, 3.003}; //Test: stock price goes down all day. Expected result: $0.000
    	assertEquals(0.000, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }  
	   
    public void test3_stockPriceHighInMiddle()
    {
    	double[] stock_prices_yesterday =  {400.001, 1500.002, 119200.00, 14000, 23000.003}; //Test: highest stock price is in the middle of the day. Expected result: $118799.999
    	assertEquals(118799.999, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }  
    
    public void test4_stockPriceSameAllDay()
    {
    	double[] stock_prices_yesterday =  {400.001, 400.001, 400.001, 400.001, 400.001}; //Test: stock market price did not change all day. Expected result: $0.000
    	assertEquals(0.000, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }  
    
    public void test5_only3prices()
    {
    	double[] stock_prices_yesterday =  {0.023, 1500.002, 0.023}; //Expected result: $1499.979
    	assertEquals(1499.979, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 3));
    }  
    
    public void test6_minimum2ndMaxLast()
    {
    	double[] stock_prices_yesterday =  {4400.001, 150.002, 9200.003, 4000.000, 23000.003}; //Expected result: $22850.001
    	assertEquals(22850.001, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }  
    
    public void test7_lowestPriceLast()
    {
    	double[] stock_prices_yesterday =  {400.001, 1500.002, 1.500, 20.000, 1.003}; //Test: lowest price is last index. Expected result: $1100.001
   	 	assertEquals(1100.001, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }
    
    public void test8_determineMinutesOpen()
    {
   	 	assertEquals(2, MaxDailyStockProfit.determineMinutesOpen( "10:00", "10:02"));
    }  
    
    public void test9_determineMinutesOpen()
    {
   	 	assertEquals(360, MaxDailyStockProfit.determineMinutesOpen( "10:00", "16:00"));
    } 
    
    public void test10_maxPriceFirst()
    {
    	double[] stock_prices_yesterday =  {5.0, 2.0, 1.0, 4.000, 0.500}; //Test: highest stock price is 1st entry. Expected result: $3.000
    	assertEquals(3.000, MaxDailyStockProfit.bestProfitTest(stock_prices_yesterday, 5));
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
