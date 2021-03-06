package entitydata;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * Serializable HomeLoan object.
 * 
 * @author Peter (s3286430)
 */
public class HomeLoan implements Serializable
{
	private int mCustomerId;
	private String mCurrentJob;
	private String mSalaryYear;
	private String mContactPhone;
	private String mContactEmail;
	private int mContactType;
	private BigDecimal mAmountBorrowed;
	private BigDecimal mAmountRepayed;
	
	public HomeLoan(int aCustomerId, String aCurrentJob, String aSalaryYear, 
			String aContactPhone,
			String aContactEmail,
			int aContactType, BigDecimal aAmountBorrowed, BigDecimal aAmountRepayed)
	{
		mCustomerId = aCustomerId;
		mCurrentJob = aCurrentJob;
		mSalaryYear = aSalaryYear;
		mContactPhone = aContactPhone;
		mContactEmail = aContactEmail;
		mContactType = aContactType;
		mAmountBorrowed = aAmountBorrowed;
		mAmountRepayed = aAmountRepayed;
	}
	
	public String getCurrentJob()
	{
		return mCurrentJob;
	}

	public String getSalaryYear()
	{
		return mSalaryYear;
	}
	public String getContactPhone()
	{
		return mContactPhone;
	}

	public String getContactEmail()
	{
		return mContactEmail;
	}
	public int getContactType()
	{
		return mContactType;
	}

	public BigDecimal getAmountBorrowed()
	{
		return mAmountBorrowed;
	}

	public BigDecimal getAmountRepayed()
	{
		return mAmountRepayed;
	}
}
