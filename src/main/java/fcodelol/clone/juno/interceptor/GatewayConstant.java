package fcodelol.clone.juno.interceptor;



import fcodelol.clone.juno.dto.ApiEntity;

import java.util.ArrayList;
import java.util.List;

public class GatewayConstant {
    public static  List<ApiEntity> apiEntities = new ArrayList<>();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final long validAuthenticationTime = 1800000;
    public static void addApiEntities(){
        apiEntities.add(new ApiEntity("getAllBill","/bill","GET","ADMIN"));
        apiEntities.add(new ApiEntity("setBillStatus","/bill/update/status","PUT","ADMIN"));
        apiEntities.add(new ApiEntity("deleteBill","/bill/delete/*","PUT","ADMIN"));
        apiEntities.add(new ApiEntity("statistic","/statistic/**","GET","ADMIN"));
        apiEntities.add(new ApiEntity("productPost","/product/**","POST","ADMIN"));
        apiEntities.add(new ApiEntity("productPut","/product/**","PUT","ADMIN"));
        apiEntities.add(new ApiEntity("productDelete","/product/**","DELETE","ADMIN"));
        apiEntities.add(new ApiEntity("removeBill","/buy/delete/product/**","PUT","ADMIN"));
        apiEntities.add(new ApiEntity("updateStatus","/update/status","PUT","ADMIN"));
    }
}
