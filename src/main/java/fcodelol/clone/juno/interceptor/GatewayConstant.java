package fcodelol.clone.juno.interceptor;



import fcodelol.clone.juno.dto.ApiEntity;

import java.util.ArrayList;
import java.util.List;

public class GatewayConstant {

    protected static final  List<ApiEntity> apiEntities = new ArrayList<>();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final long VALID_AUTHENTICATION_TIME = 1800000;

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_MEMBER = "MEMBER";
    public static final String ROLE_GUESS = "GUESS";
    protected static final String ROLE_SPLIT_STRING = "&";
    protected static final String ALL_PRODUCT_URL_PATTERN = "/product/**";
    protected static final String ALL_DISCOUNT_URL_PATTERN = "/discount/**";
    private GatewayConstant() {
    }

    public static void addApiEntities(){
        apiEntities.add(new ApiEntity("getAllBill","/bill","GET",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("setBillStatus","/bill/update/status","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("deleteBill","/bill/delete/*","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("getIncomes","/statistic/income","GET",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("getTopCustomer","/statistic/top/customer/*","GET",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("productPost",ALL_PRODUCT_URL_PATTERN,"POST",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("productPut",ALL_PRODUCT_URL_PATTERN,"PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("productDelete",ALL_PRODUCT_URL_PATTERN,"DELETE",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("removeBill","/bill/delete/product/**","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("getAllBillOfUser","/bill/user/**","PUT",combineRoles(ROLE_ADMIN,ROLE_MEMBER)));
        apiEntities.add(new ApiEntity("getBillById","/bill/*","PUT",combineRoles(ROLE_ADMIN,ROLE_MEMBER)));
        apiEntities.add(new ApiEntity("getAllBillOfUser","/bill/user/**","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("updateStatus","/update/status","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("addDiscount",ALL_DISCOUNT_URL_PATTERN,"POST",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("updateDiscount",ALL_DISCOUNT_URL_PATTERN,"PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("deleteDiscount",ALL_DISCOUNT_URL_PATTERN,"DELETE",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("banAndUnbanUser","/user/**","PUT",ROLE_ADMIN));
        apiEntities.add(new ApiEntity("getAllUser","/user","GET",ROLE_ADMIN));
    }
    private static String combineRoles(String... roles){
        StringBuilder builder = new StringBuilder();
        for(String role: roles){
            builder.append(ROLE_SPLIT_STRING + role);
        }
        //remove the first role split string
        return builder.toString().substring(1);
    }
}
