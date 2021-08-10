package fcodelol.clone.juno.interceptor;



import fcodelol.clone.juno.dto.ApiEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class GatewayConstant {
    public static  List<ApiEntity> apiEntities = new ArrayList<>();
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final long validAuthenticationTime = 1800000;
    public static void addApiEntities(){

    }
}
