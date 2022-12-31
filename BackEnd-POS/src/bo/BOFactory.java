package bo;

import bo.custom.impl.CustomerBOImpl;
import bo.custom.impl.QueryBOImpl;

/**
 * @author : Nimesh Piyumantha
 * @since : 0.1.0
 **/
public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory() {
    }

    public static BOFactory getBoFactory() {
        return boFactory == null ? boFactory = new BOFactory() : boFactory;
    }

    public SuperBO getBO(BOTypes types) {
        switch (types) {
            case CUSTOMER:
                return new CustomerBOImpl();
            case CUSTOM:
                return new QueryBOImpl();

            default:
                return null;
        }
    }

    public enum BOTypes {
        CUSTOMER,CUSTOM
    }

}
