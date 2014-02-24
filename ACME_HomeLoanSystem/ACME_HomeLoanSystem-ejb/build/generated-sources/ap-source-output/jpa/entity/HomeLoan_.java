package jpa.entity;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.0.v20130507-rNA", date="2013-10-17T17:37:33")
@StaticMetamodel(HomeLoan.class)
public class HomeLoan_ { 

    public static volatile SingularAttribute<HomeLoan, Long> id;
    public static volatile SingularAttribute<HomeLoan, String> mContactPhone;
    public static volatile SingularAttribute<HomeLoan, BigDecimal> mAmountBorrowed;
    public static volatile SingularAttribute<HomeLoan, BigDecimal> mAmountRepayed;
    public static volatile SingularAttribute<HomeLoan, String> mContactEmail;
    public static volatile SingularAttribute<HomeLoan, String> mSalaryYear;
    public static volatile SingularAttribute<HomeLoan, Integer> mCustomerId;
    public static volatile SingularAttribute<HomeLoan, String> mCurrentJob;
    public static volatile SingularAttribute<HomeLoan, Integer> mContactType;

}