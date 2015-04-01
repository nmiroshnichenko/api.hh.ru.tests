package ru.hh.api.tests.common;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ru.hh.api.utils.Log;

/**
 * Created by user on 30.03.15 18:28.
 */
public class CommonTestListener extends Log implements ITestListener {

  /**
   * Invoked each time before a test will be invoked.
   * The <code>ITestResult</code> is only partially filled with the references to
   * class, , start millis and status.
   *
   * @param result the partially filled <code>ITestResult</code>
   * @see org.testng.ITestResult#STARTED
   */
  @Override
  public void onTestStart(ITestResult result) {
    info("test method '" + result.getMethod().getMethodName() + "' started");
  }

  /**
   * Invoked each time a test succeeds.
   *
   * @param result <code>ITestResult</code> containing information about the run test
   * @see org.testng.ITestResult#SUCCESS
   */
  @Override
  public void onTestSuccess(ITestResult result) {
    info("test method '" + result.getMethod().getMethodName() + "' OK");
  }

  /**
   * Invoked each time a test fails.
   *
   * @param result <code>ITestResult</code> containing information about the run test
   * @see org.testng.ITestResult#FAILURE
   */
  @Override
  public void onTestFailure(ITestResult result) {
    info("test method '" + result.getMethod().getMethodName() + "' failed");
  }

  /**
   * Invoked each time a test is skipped.
   *
   * @param result <code>ITestResult</code> containing information about the run test
   * @see org.testng.ITestResult#SKIP
   */
  @Override
  public void onTestSkipped(ITestResult result) {
    info("test method '" + result.getMethod().getMethodName() + "' skipped");
  }

  /**
   * Invoked each time a  fails but has been annotated with
   * successPercentage and this failure still keeps it within the
   * success percentage requested.
   *
   * @param result <code>ITestResult</code> containing information about the run test
   * @see org.testng.ITestResult#SUCCESS_PERCENTAGE_FAILURE
   */
  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

  }

  /**
   * Invoked after the test class is instantiated and before
   * any configuration  is called.
   *
   * @param context
   */
  @Override
  public void onStart(ITestContext context) {
    info("test '" + context.getName() + "' initialized");
  }

  /**
   * Invoked after all the tests have run and all their
   * Configuration s have been called.
   *
   * @param context
   */
  @Override
  public void onFinish(ITestContext context) {
    info("test '" + context.getName() + "' finished");
  }
}
